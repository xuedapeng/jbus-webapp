
window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    deviceId:getQueryString('deviceId'),
    branchId:'',
    deviceSn:'',
    deviceName:'',
    location:'',
    latitude:'',
    longitude:'',
    description:'',
    branchName:'',
  },
  watch: {

  },
  methods:{
    delDevice(){
      if (confirm("是否确认删除设备？\n注意：删除后不可恢复。")){
        deleteDevice();
      }
    },
    editDevice(){
      moveTo("deviceUpdate.html?deviceId="+app.deviceId);
    },
  }
});

function getData() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"sysman.device.detail",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{"deviceId":app.deviceId}};

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:1000});
            return;
          }

          app.branchId = response.branchId;
          app.deviceSn = response.deviceSn;
          app.deviceName = response.deviceName;
          app.location = response.location;
          app.latitude = response.latitude;
          app.longitude = response.longitude;
          app.description = response.description;
          app.branchName = response.branchName;
      });
}

function deleteDevice() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"sysman.device.del",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  "data":{
                    "deviceId":app.deviceId,
                  }};

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:1000});
            return;
          }

          layer.msg("设备删除成功！", {icon:1,time:1000});

          setTimeout("moveTo('result.html?title=设备删除成功')", 2000);
      });
}
