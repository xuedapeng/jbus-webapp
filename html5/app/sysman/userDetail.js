
window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    userId:getQueryString("userId"),
    account:'',
    nickName:'',
    branchRole:1,
    branchName:'',
    status:'',
  },
  watch: {

  },
  methods:{
    delUser(){
      if (confirm("是否确认删除用户？\n注意：删除后不可恢复。")){
        deleteUser();
      }
    },
    editUser(){
      moveTo("userUpdate.html?userId="+app.userId);
    },
  }
});


function getData() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.user.detail",
                "auth":[localStorage.secretId, localStorage.secretKey],
                "data":{
                  "userId":app.userId,
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
    });
}

function deleteUser() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"sysman.user.del",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{
                    "userId":app.userId,
                  }};

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:1000});
            return;
          }

          layer.msg("人员删除成功！", {icon:1,time:1000});

          setTimeout("moveTo('result.html?title=人员删除成功')", 2000);
      });
}
