window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    defalut_center:[37.443663, 116.429706],
    deviceList:[],
    deviceSnList:[],
    selectedDeviceSn:'',
    selectedDeviceName:'',
  },
  computed: {
    // 计算属性的 getter
    mapCenter: function () {
      if (this.deviceList.length == 0) {
        return this.defalut_center;
      }

      var center = getCenter();
      console.log("mapCenter=" + center);
      return center;
    }
  },
  methods:{

  }
});

function getData() {

      if (!checkAuth()) {
         return;
      }

      var param = {"method":"devman.mydevice.list",
                  "auth":[localStorage.secretId, localStorage.secretKey],
                  };

      ajaxPost(G_API_URL, param,
        function(response){

          if (response.status<0) {
            layer.msg(response.msg, {icon:2,time:1000});
            return;
          }

          app.branchDeviceList = response.result;

  				app.deviceList = [];
          app.deviceSnList = [];
          var deviceList = [];
  				for (var i in app.branchDeviceList) {
  					for (var j in app.branchDeviceList[i].deviceList) {
  							var item = app.branchDeviceList[i].deviceList[j];
                item.instantFlow = "-";
                item.realHeight = "-";
                item.status = "off";
  							deviceList.push(item);
                app.deviceSnList.push(item.deviceSn);
  					}
  				}
          app.deviceList = deviceList;

          showMap();
          ws_connect();
      });
}

var myMap; // loadmap.js 中赋值
function showMap() {
  var mapsource=1;// 1:chinaProvider;2:mapbox
  var center = app.mapCenter;

  var mapParam = {"center":center ,"divId":"mapId"};
  if (mapsource==1) {
    loadChinaProvider(mapParam);
  } else {
    loadMapbox(mapParam);
  }

  for (var i in app.deviceList) {
    var item = app.deviceList[i];
    var gps = [parseFloat(item.latitude), parseFloat(item.longitude)];

    var marker = L.marker(gps).addTo(myMap);

    app.deviceList[i].marker = marker;
    makeMarker(app.deviceList[i]);

  }
}

function updateMap() {
  for(var i in app.deviceList) {
    var item = app.deviceList[i];
    makeMarker(item);
  }
}

function makeMarker(deviceItem) {

    var popMsg = deviceItem.deviceName
                + "<br/> 瞬时流量："
                + deviceItem.instantFlow + " m³</sup>/s"
                + "<br/> 当前闸位："
                + deviceItem.realHeight+ " m"
                + "<br/> <a href='javascript:open(" + deviceItem.deviceId + ");'> 查看 </a>";
    deviceItem.marker.bindPopup(popMsg);
    deviceItem.marker.bindTooltip(deviceItem.deviceName, {
      permanent:true,
      offset : [ 0, 0 ],// 偏移
      direction : "right",// 放置位置
      sticky:true,//是否标记在点上面
    }).openTooltip();
}

function open(deviceId) {

  for (var i in app.deviceList) {
    var item = app.deviceList[i];
    if (deviceId == item.deviceId) {
      app.selectedDeviceSn = item.deviceSn;
      app.selectedDeviceName = item.deviceName;
    }
  }
  $("#id_open").attr('data-href',
                    'app/control.html?deviceSn=' + app.selectedDeviceSn
                    + '&deviceName='+ app.selectedDeviceName);
  $("#id_open").attr('data-title',
                    app.selectedDeviceName);

  document.getElementById("id_open").click();
}
function getCenter() {

        var maxLat = 0;
        var minLat = 1000;
        var maxLng = 0;
        var minLng = 1000;

        for(var i in app.deviceList) {
          var item = app.deviceList[i];
          if (item.latitude > maxLat) {
            maxLat = item.latitude;
          }
          if (item.latitude < minLat) {
            minLat = item.latitude;
          }
          if (item.longitude > maxLng) {
            maxLng = item.longitude;
          }
          if (item.longitude < minLng) {
            minLng = item.longitude;
          }
        }

        return [(parseFloat(maxLat)+parseFloat(minLat))/2
                , (parseFloat(maxLng)+parseFloat(minLng))/2];
}


// websocket start
var ws = null ;
var target=G_WS_URL;
function ws_connect() {

  if ('WebSocket' in window) {
      ws = new WebSocket(target);
  } else if ('MozWebSocket' in window) {
      ws = new MozWebSocket(target);
  } else {
      alert('WebSocket is not supported by this browser.');
  }

  ws.onopen = function(obj){
      console.info('open') ;
      console.info(obj);
      ws_signIn();
  } ;

  ws.onclose = function (obj) {
      console.info('close') ;
      console.info(obj) ;
  } ;
  ws.onmessage = function(obj){

      var result = JSON.parse(obj.data);

      console.log(JSON.stringify(result));

      if (result.topicType == 'TC/STS/') {

        var deviceSn = result.deviceSn;
        var status = result.data.event;
        var deviceList = app.deviceList;
        for (var i in deviceList) {
          if (deviceList[i].deviceSn == deviceSn) {
            deviceList[i].status = status;
            if (deviceList[i].status == "off") {
              deviceList[i].instantFlow = "-";
              deviceList[i].realHeight = "-";
            }
          }
        }
        app.deviceList = deviceList;
        updateMap();
        return;
      }

      if (result.topicType == 'TC/DAT/') {

          if (!(result.data && result.data.type)) {
            return;
          }

          if (result.data.type == "GET_INFO") {
            var deviceSn = result.deviceSn;
            var deviceList = app.deviceList;
            for (var i in deviceList) {
              if (deviceList[i].deviceSn == deviceSn) {
                  deviceList[i].instantFlow = result.data.instantFlow;
                  deviceList[i].realHeight = result.data.realHeight;
              }
            }
            app.deviceList = deviceList;
          }

          updateMap();
          return;
      }
  } ;

}

function ws_signIn(){
  var param = {
              "msgType":"sign_in",
              "path":"status",
              "secretId":localStorage.secretId,
              "secretKey":localStorage.secretKey,
              "deviceSnList":app.deviceSnList
  };

  var pstr = JSON.stringify(param);
	ws.send(pstr);
  console.log(pstr);
}
