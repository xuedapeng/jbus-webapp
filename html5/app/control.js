
window.onload = function() {
  getOnlineStatus();
  ws_connect();
  setFlowGraph();
}

var app = new Vue({
  el: '#app',
  data: {
    doorName:getQueryString('deviceName'),
    deviceSn:getQueryString('deviceSn'),
    expectHeight:'-',
    realHeight:'-',
    maxHeight:G_MAX_HEIGHT_M,
    instantFlow:'-',
    totalFlow:'-',
    frontWaterline:'-',
    backWaterline:'-',
    electricCurrent:'-',
    electricVoltage:'-',
    totalRunningTime:'-',
    online:false,
    instantFlowHist:[],

  },
  watch: {
    realHeight: function(curVal,oldVal){
        console.log("oldVal:" + oldVal);
        console.log("curVal:" + curVal);
        moveDoor(curVal);
    },

    instantFlow: function(curVal,oldVal){
      waterFlowAnimate();
    }
  },
  methods:{
    start() {
      ws_cmdOpen(app.expectHeight);
      console.log("send cmd: start to " + app.expectHeight);
    },
    stop() {
      if (confirm("是否停止？")) {
        ws_cmdStop();
        console.log("send cmd: stop of " + app.expectHeight);
      }
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

function flowControl() {
  alert("流量控制（功能待定）");
}

function totolControl() {
  alert("总量控制（功能待定）");
}
function deviceInfo() {
  $("#modal-deviceInfo").modal("show");

}
function troubleAlert() {
  alert("故障报警（功能待定）");
}


function getOnlineStatus() {

	    if (!checkAuth()) {
	       return;
	    }

	    var param = {"method":"devman.onlinestatus.get",
	                "auth":[localStorage.secretId, localStorage.secretKey],
									"data":{
										"deviceSnList":[app.deviceSn],
									}};

	    ajaxPost(G_API_URL, param,
	      function(response){

	        if (response.status<0) {
	          layer.msg(response.msg, {icon:2,time:1000});
	          return;
	        }

					app.online = response.result[app.deviceSn]=="on"?true:false;

	    });
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

      if (result.topicType == 'TC/STS/') {
        app.online = (result.data.event == "on"?true:false);
        return;
      }

      if (!(result.data && result.data.type)) {
        return;
      }


      if (result.data.type == "GET_INFO") {
        console.log("result.data.realHeight=" + result.data.realHeight);
        app.realHeight = result.data.realHeight;
        app.expectHeight = result.data.expectHeight;
        app.electricCurrent = result.data.electricCurrent;
        app.electricVoltage = result.data.electricVoltage;
        app.totalRunningTime = result.data.runHours + " 小时 " + result.data.runMinutes + " 分钟";
        app.instantFlow = result.data.instantFlow;
        app.totalFlow = result.data.totalFlow;

        var now = dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        app.instantFlowHist.push(
              {"name":now,
              "value":[now , app.instantFlow]});

        updateInstantFlow();
      }

  } ;

}

function ws_signIn(deviceSn){
  var param = {
              "msgType":"sign_in",
              "path":"gate",
              "secretId":localStorage.secretId,
              "secretKey":localStorage.secretKey,
              "deviceSn":app.deviceSn
  };

  var pstr = JSON.stringify(param);
	ws.send(pstr);
  console.log(pstr);
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
  var pstr = JSON.stringify(param);
	ws.send(pstr);
  console.log(pstr);
}

// 水波动画

var _FLOW_IMG0 = "../static/img/water.png";
var _FLOW_IMG1 = "../static/img/waterFlow.gif";
var _FLOW_IMG2 = "../static/img/waterFlow2.gif";
function waterFlowAnimate() {
    if (app.instantFlow == '-' || app.instantFlow == 0) {
      $("#img_waterflow").attr("src",_FLOW_IMG0);
      return;
    }

    if ($("#img_waterflow").attr("src") == _FLOW_IMG1) {
      $("#img_waterflow").attr("src",_FLOW_IMG2);
    } else {
      $("#img_waterflow").attr("src",_FLOW_IMG1);
    }

    setTimeout("waterFlowAnimate()", 1500);
}

// 瞬时流量过程线
// 基于准备好的dom，初始化echarts实例
function getAvgFlowData() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"stats.avgflow.lasthour",
                "auth":[localStorage.secretId, localStorage.secretKey],
								"data":{
									"deviceSn":app.deviceSn,
								}};

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        for(var i in response.result) {
          var item = response.result[i];
          app.instantFlowHist.push(
              {"name":item.time,
                "value":[item.time, item.avgFlow],
              });
        }

        setFlowGraph();
    });
}

var myChart ;
function setFlowGraph() {

  var option = {
      tooltip: {
          trigger: 'axis',
          formatter: function (params) {
              params = params[0];
              return '[' + params.value[0] + ']  ' + params.value[1];
          },
          axisPointer: {
              animation: false
          }
      },
      xAxis: {
          type: 'time',
          splitLine: {
              show: false
          }
      },
      yAxis: {
          type: 'value',
          boundaryGap: [0, '100%'],
          splitLine: {
              show: false
          }
      },
      series: [{
          name: '模拟数据',
          type: 'line',
          showSymbol: false,
          hoverAnimation: false,
          data: app.instantFlowHist,
      }]
  };

  myChart = echarts.init(document.getElementById('flowGraph'));
  myChart.setOption(option);
}

function updateInstantFlow() {
  console.log("app.instantFlowHist.length:" + app.instantFlowHist.length);
  myChart.setOption({
      series: [{
          data: app.instantFlowHist,
      }]
  });
}
