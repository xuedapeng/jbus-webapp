
window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    deviceId:getQueryString('deviceId'),
    deviceSn:'',
    deviceName:'',
    location:'',
    latitude:'',
    longitude:'',
    branchId:'',
    description:'',
    branchList:[],
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

      if (confirm("是否修改设备信息？")){
        updateDevice();
      }

    },

    reset() {
      getData();
      clearError();
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

          app.deviceSn = response.deviceSn;
          app.deviceName = response.deviceName;
          app.location = response.location;
          app.latitude = response.latitude;
          app.longitude = response.longitude;
          app.branchId = response.branchId+'';
          app.description = response.description;
          app.branchName = response.branchName;

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

function updateDevice() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.device.update",
                "auth":[localStorage.secretId, localStorage.secretKey],
                "data":{
                  "deviceId":app.deviceId,
                  "deviceSn":app.deviceSn,
                  "deviceName":app.deviceName,
                  "location":app.location,
                  "latitude":app.latitude,
                  "longitude":app.longitude,
                  "branchId":app.branchId,
                  "description":app.description

                }};

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        layer.msg("设备修改成功！", {icon:1,time:1000});

        setTimeout("moveTo('deviceDetail.html?deviceId=" + response.deviceId + "')", 2000);
    });
}
