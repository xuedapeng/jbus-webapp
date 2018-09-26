
window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    userId:getQueryString("userId"),
    nickName:getQueryString("nickName"),
    branchPrivlegeList:[],
  },
  watch: {

  },
  methods:{

      ok() {
        updatePrivilege();
      },

      reset() {
        getData();
      }
  }
});

function getData() {
  getBranch();
}

function getBranch() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.branch.list",
                "auth":[localStorage.secretId, localStorage.secretKey],
              };

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        var branchList = response.result;

        var brprList = [];
        for(i in branchList) {
          var br = branchList[i];
          var brpr = {"branchId":br.branchId,
                      "branchName":br.branchName,
                      "privilege":'0',
                    };
          brprList.push(brpr);
        }
        app.branchPrivlegeList = brprList;

        getPrivilege();


    });
}

function getPrivilege() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"sysman.privilege.user.get",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{
                    "userId":app.userId,
                  }
                };

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:1000});
            return;
          }

          var privilegeList = response.result;

          var brprList = app.branchPrivlegeList;

          for(i in brprList) {
            var brpr = brprList[i];
            for(j in privilegeList) {
              var pr = privilegeList[j];
              if (pr.objectType==2 && pr.objectId == brpr.branchId) {
                brpr.privilege = pr.privilege + '';
              }
            }
          }

          app.branchPrivlegeList = brprList;

      });
}

function updatePrivilege() {

        if (!checkAuth()) {
           return;
        }

        var privileges = [];
        for (i in app.branchPrivlegeList) {
          var item = app.branchPrivlegeList[i];
          if (item.privilege > 0) {
            privileges.push(item);
          }
        }


        var param = {"method":"sysman.privilege.user.update",
                    "auth":[localStorage.secretId, localStorage.secretKey],
                    "data":{
                      "userId":app.userId,
                      "privileges":privileges,
                    }
                  };

        ajaxPost(G_API_URL, param,
          function(response){

            if (response.status<0) {
              layer.msg(response.msg, {icon:2,time:1000});
              return;
            }


            layer.msg("权限修改成功！", {icon:1,time:1000});

            setTimeout("getData()", 2000);

        });
}
