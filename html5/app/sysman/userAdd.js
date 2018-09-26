
window.onload = function() {

}

var app = new Vue({
  el: '#app',
  data: {
    branchId:getQueryString("branchId"),
    branchName:getQueryString("branchName"),
    account:'',
    password:'',
    nickName:'',
    email:'',
    branchRole:1,
    errorInfo:'',
  },
  watch: {

  },
  methods:{
    ok() {
      clearError();
      if (!validate()) {
        return;
      }

      addUser();

    },

    reset() {
      app.account = '';
      app.password = '';
      app.nickName = '';
      app.email = '';
      app.branchRole = 1;
      app.errorInfo = '';
    }
  }
});

function clearError() {
  app.errorInfo = '';
}

function validate() {

  if (app.nickName=='') {
    app.errorInfo = "请输入姓名！";
    return false;
  }
  if (app.account=='') {
    app.errorInfo = "请输入帐号！";
    return false;
  }
  if (app.password=='') {
    app.errorInfo = "请输入密码！";
    return false;
  }

  return true;
}

function addUser() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.user.add",
                "auth":[localStorage.secretId, localStorage.secretKey],
                "data":{
                  "account":app.account,
                  "password":app.password,
                  "nickName":app.nickName,
                  "email":app.email,
                  "branchRole":app.branchRole,
                  "branchId":app.branchId,
                }};

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        layer.msg("人员添加成功！", {icon:1,time:1000});

        setTimeout("moveTo('userDetail.html?userId=" + response.userId + "')", 2000);
    });
}
