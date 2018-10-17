
window.onload = function() {
	if (!checkAuth()) {
			moveToLogin();
      return;
	}
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
		account:localStorage.account,
		nickName:localStorage.nickName,
		isSysAdmin:localStorage.isSysAdmin,
		isBranchAdmin:localStorage.isBranchAdmin,
		title: G_LABEL_APP_TITLE,
		branchDeviceList:[],
		deviceSnList:[],
		deviceOnlineStatusMap:{},
	},
	methods:{
		logout:function(){
			localStorage.clear();
			moveToLogin();
		}
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

				app.deviceSnList = [];
				for (var i in app.branchDeviceList) {
					for (var j in app.branchDeviceList[i].deviceList) {
							var item = app.branchDeviceList[i].deviceList[j];
							app.deviceSnList.push(item.deviceSn);
					}
				}
				setTimeout("makeMenuEnable()", 1000);
				getOnlineStatus();
    });
}

function getOnlineStatus() {

	    if (!checkAuth()) {
	       return;
	    }

	    var param = {"method":"devman.onlinestatus.get",
	                "auth":[localStorage.secretId, localStorage.secretKey],
									"data":{
										"deviceSnList":app.deviceSnList,
									}};

	    ajaxPost(G_API_URL, param,
	      function(response){

	        if (response.status<0) {
	          layer.msg(response.msg, {icon:2,time:1000});
	          return;
	        }
					app.deviceOnlineStatusMap = response.result;
					updateOnlineStatus();
          ws_connect();
	    });
}

function updateOnlineStatus() {
	$(".deviceIcon").each(function(i, v){
		var sn = this.id.replace("icon_", "");
		var color = app.deviceOnlineStatusMap[sn]=="on"?"#0c0":"grey";
		$(this).css("color", color);
	});
}

function makeMenuEnable() {
	$(".Hui-aside").on("click",".menu_dropdown dd li a",function(){
		if($(window).width()<768){
			$(".Hui-aside").slideToggle();
		}
	});
	/*左侧菜单*/
	$(".Hui-aside").Huifold({
		titCell:'.menu_dropdown dl dt',
		mainCell:'.menu_dropdown dl dd',
	});

	/*选项卡导航*/
	$(".Hui-aside").on("click",".menu_dropdown a",function(){
		Hui_admin_tab(this);
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

      var result = JSON.parse(obj.data);

      console.log(JSON.stringify(result));

      if (result.topicType != 'TC/STS/') {
        return;
      }

      var deviceSn = result.deviceSn;
      var status = result.data.event;

      app.deviceOnlineStatusMap[deviceSn]=status;
      updateOnlineStatus();
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
