
window.onload = function() {

}

var app = new Vue({
  el: '#app',
  data: {
    branchId:getQueryString("branchId"),
    branchName:getQueryString("branchName"),
    deviceSn:'',
    deviceName:'',
    location:'',
    latitude:'',
    longitude:'',
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

      addDevice();

    },

    reset() {
      app.deviceSn = '';
      app.deviceName = '';
      app.location = '';
      app.latitude = '';
      app.longitude = '';
      app.description = '';
      app.errorInfo = '';
    }
  }
});

function clearError() {
  app.errorInfo = '';
}

function validate() {
  if (app.deviceSn=='') {
    app.errorInfo = "请输入设备编号！";
    return false;
  }
  if (app.deviceName=='') {
    app.errorInfo = "请输入设备名称！";
    return false;
  }
  if (app.location=='') {
    app.errorInfo = "请输入所在地址！";
    return false;
  }
  if (app.latitude=='') {
    app.errorInfo = "请输入纬度！";
    return false;
  }
  if (app.longitude=='') {
    app.errorInfo = "请输入经度！";
    return false;
  }

  return true;
}

function addDevice() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.device.add",
                "auth":[localStorage.secretId, localStorage.secretKey],
                "data":{
                  "branchId":app.branchId,
                  "deviceSn":app.deviceSn,
                  "deviceName":app.deviceName,
                  "location":app.location,
                  "latitude":app.latitude,
                  "longitude":app.longitude,
                  "description":app.description

                }};

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        layer.msg("设备添加成功！", {icon:1,time:1000});

        setTimeout("moveTo('deviceDetail.html?deviceId=" + response.deviceId + "')", 2000);
    });
}
