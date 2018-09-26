
window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    branchId:getQueryString('branchId'),
    branchName:'',
    description:'',
  },
  watch: {

  },
  methods:{

      delBranch(){
        if (confirm("是否确认删除管理单位？\n注意：删除后不可恢复。")){
          deleteBranch();
        }
      },
      editBranch(){
        moveTo("branchUpdate.html?branchId="+app.branchId);
      },
  }
});

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
            layer.msg(response.msg, {icon:2,time:2000});
            return;
          }

          app.branchName = response.branchName;
          app.description = response.description;
      });
}

function deleteBranch() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"sysman.branch.del",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{
                    "branchId":app.branchId
                  }};

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:2000});
            return;
          }

          layer.msg("管理单位删除成功！", {icon:1,time:2000});

          setTimeout("moveTo('result.html?title=管理单位删除成功')", 2000);
      });
}
