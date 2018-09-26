
window.onload = function() {
  getData();
  getBranchList();
}

var app = new Vue({
  el: '#app',
  data: {
    userId:getQueryString("userId"),
    account:'',
    nickName:'',
    branchId:'',
    branchRole:1,
    status:'',
    isUpdatePwd:false,
    password:'',
    errorInfo:'',
    branchList:[],
  },
  watch: {

  },
  methods:{
    ok(){
      clearError();
      if (!validate()) {
        return;
      }

      if (confirm("是否修改用户？")){
        updateUser();
      }
    },
    reset(){
      getData();
      clearError();
    },
  }
});

function clearError() {
  app.errorInfo = '';
}

function validate() {

  if (app.account=='') {
    app.errorInfo = "请输入帐号！";
    return false;
  }

  if (app.nickName=='') {
    app.errorInfo = "请输入姓名！";
    return false;
  }

  if (app.isUpdatePwd && app.password=='') {
    app.errorInfo = "请输入新密码！";
    return false;
  }

  return true;
}

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
        app.branchId = response.branchId;
        app.branchName = response.branchName;
        app.status = response.status;

        getBranchList();
    });
}

function getBranchList() {

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

        result = response.result;
        app.branchList = result;

    });
}

function updateUser() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"sysman.user.update",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{
                    "userId":app.userId,
                    "account":app.account,
                    "nickName":app.nickName,
                    "branchId":app.branchId+'',
                    "branchRole":app.branchRole+'',
                    "status":app.status+'',
                    "password":app.isUpdatePwd?app.password+'':"",

                  }};

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:1000});
            app.errorInfo = response.msg;
            return;
          }

          layer.msg("人员修改成功！", {icon:1,time:1000});

          setTimeout("moveTo('userDetail.html?userId="+ app.userId +"')", 2000);
      });
}
