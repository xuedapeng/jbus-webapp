
window.onload = function() {
  ws_connect();
}

var app = new Vue({
  el: '#app',
  data: {
    doorName:'潘庄渠首 1号闸门',
    deviceSn:getQueryString('deviceSn'),
    expectHeight:'',
    realHeight:'-',
    maxHeight:G_MAX_HEIGHT_M,
    instantFlow:'-',
    frontWaterline:'-',
    backWaterline:'-',
    electricCurrent:'-',
    electricVoltage:'-',
    runningTime:'-',
    totalRunningTime:'-',

  },
  watch: {
    realHeight: function(curVal,oldVal){
        console.log("oldVal:" + oldVal);
        console.log("curVal:" + curVal);
        moveDoor(curVal);
    },
  },
  methods:{
    start() {
      ws_cmdOpen(app.expectHeight);
      console.log("send cmd: start to " + app.expectHeight);
    },
    stop() {
      ws_cmdStop();
      console.log("send cmd: stop of " + app.expectHeight);
    },
    oninputExpect(e) {
      this.val=e.target.value.replace(/[^\d|.]/g,'');
      if (this.val > app.maxHeight) {
        this.val = app.maxHeight;
      }

      app.expectHeight = this.val;
      console.log('app.expectHeight',app.expectHeight);
    }
  }
});

function moveDoor(h) {
  console.log("move door to "  + h);
  var hpx = h*G_HEIGHT_MPP;
  var top = G_MAX_HEIGHT_PX - hpx;
  $(".doorDiv").animate({"height":hpx + "px", "top":top + "px"}, 1000);
  // $(".doorDiv").css("height", hpx + "px");
  // $(".doorDiv").css("top", top + "px");
  var labelTop = G_MAX_HEIGHT_PX - hpx - 20;
  $("#doorLabel").animate({"top":labelTop + "px"}, 1000);
  // $("#doorLabel").css("top", labelTop + "px");
}

function openControl() {
  $("#modal-openControl").modal("show");
}

function openControlConfirm() {
  if (app.expectHeight == '' || !isNumber(app.expectHeight) || app.expectHeight<0 || app.expectHeight>app.maxHeight) {
    app.expectHeight = '';
    layer.msg("无效的开度值！", {icon:1,time:1000});
    return;
  }

  app.start();
  $("#modal-openControl").modal("hide");
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
      console.info(obj) ;
      console.info(obj.data);

      var result = JSON.parse(obj.data);
      console.log("result.data=" + result.data);


      if (!(result.data && result.data.type)) {
        return;
      }

      if (result.data.type == "GET_HEIGHT") {
        console.log("result.data.height=" + result.data.height);
        app.realHeight = result.data.height/1000;
      }

      if (result.data.type == "GET_FLOW") {
        console.log("result.data.flow=" + result.data.flow);
        app.instantFlow = result.data.flow;
      }
  } ;

}

function ws_signIn(deviceSn){
  var param = {
              "msgType":"sign_in",
              "path":"gate",
              "userId":localStorage.userId,
              "apiKey":localStorage.apiKey,
              "deviceSn":app.deviceSn
  };
	ws.send(JSON.stringify(param));
}

function ws_cmdOpen(h){
  var param = {
            "cmd":"open",
            "height": Math.floor(h*1000) + '',
  };

  var pstr = JSON.stringify(param);
	ws.send(pstr);
  console.log(pstr);
}

function ws_cmdStop(){
  var param = {
            "cmd":"stop"
  };
	ws.send(JSON.stringify(param));
}
