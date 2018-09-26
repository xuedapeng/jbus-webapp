
window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    userId:localStorage.userId,
    account:'',
    nickName:'',
    branchName:'',
    branchRole:1,
    status:'',
    isUpdatePwd:false,
    oldPassword:'',
    newPassword:'',
    privileges:{},
    branchPrivlegeList:[],
    errorInfo:'',
  },
  watch: {

  },
  methods:{
    update(){
      clearError();
      if (!validate()) {
        return;
      }
      if (confirm("是否修改密码？")){
        updatePwd();
      }
    },
    reset() {
      clearError();
      app.isUpdatePwd = false;
      app.oldPassword = '';
      app.newPassword = '';
    }
  }
});

function clearError() {
  app.errorInfo = '';
}

function validate() {

  if (app.oldPassword=='') {
    app.errorInfo = "请输入旧密码！";
    return false;
  }

  if (app.newPassword=='') {
    app.errorInfo = "请输入新密码！";
    return false;
  }

  if (app.oldPassword == app.newPassword) {

      app.errorInfo = "新密码不能和旧密码相同！";
      return false;
  }
  return true;
}

function getData() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"user.myaccount.get",
                "auth":[localStorage.secretId, localStorage.secretKey],
                "data":{
                  "userId":app.userId+'',
                }};

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        app.account = response.account;
        app.nickName = response.nickName;
        app.branchRole = response.branchRole;
        app.branchName = response.branchName;
        app.status = response.status==1?"正常":"停用";
        app.privileges = response.privileges;

        getBranch();
    });
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
          if (app.privileges[br.branchId+'']) {
            var brpr = {"branchId":br.branchId,
                        "branchName":br.branchName,
                        "privilege":app.privileges[br.branchId+''],
                      };
            brprList.push(brpr);
          }
        }
        app.branchPrivlegeList = brprList;



    });
}

function updatePwd() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"user.mypassword.update",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{
                    "userId":app.userId+'',
                    "oldPassword":app.oldPassword+'',
                    "newPassword":app.newPassword+'',
                  }};

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:2000});
            return;
          }

          layer.msg("密码修改成功！", {icon:1,time:2000});

          app.isUpdatePwd = false;
          setTimeout("getData()",2000);
      });
}
