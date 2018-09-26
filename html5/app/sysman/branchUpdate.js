

window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    branchId:getQueryString("branchId"),
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

      updateBranch();

    },

    reset() {
      getData();
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

function getData() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"sysman.branch.detail",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{"branchId":app.branchId}};

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:1000});
            return;
          }

          app.branchName = response.branchName;
          app.description = response.description;
      });
}

function updateBranch() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.branch.update",
                "auth":[localStorage.secretId, localStorage.secretKey],
                "data":{
                  "branchId":app.branchId+'',
                  "branchName":app.branchName,
                  "description":app.description
                }};

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        layer.msg("管理单位修改成功！", {icon:1,time:1000});

        setTimeout("moveTo('branchDetail.html?branchId=" + response.branchId + "')", 2000);
    });
}
