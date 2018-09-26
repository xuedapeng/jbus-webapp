
window.onload = function() {

}

var app = new Vue({
  el: '#app',
  data: {
    branchName:'',
    description:'',
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

      addBranch();

    },

    reset() {
      app.branchName = '';
      app.description = '';
      app.errorInfo = '';
    }
  }
});

function clearError() {
  app.errorInfo = '';
}

function validate() {
  if (app.branchName=='') {
    app.errorInfo = "请输入单位名称！";
    return false;
  }

  return true;
}

function addBranch() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.branch.add",
                "auth":[localStorage.secretId, localStorage.secretKey],
                "data":{"branchName":app.branchName, "description":app.description}};

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        layer.msg("管理单位添加成功！", {icon:1,time:1000});

        setTimeout("moveTo('branchDetail.html?branchId=" + response.branchId + "')", 2000);
    });
}
