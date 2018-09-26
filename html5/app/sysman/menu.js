
window.onload = function() {
  getData();
}

var app = new Vue({
  el: '#app',
  data: {
    doorName:'潘庄渠首 1号闸门',
  },
  watch: {

  },
  methods:{
      addBranch(){
        setDetailSrc("branchAdd.html");
      },
      addUser(branchId, branchName){
        setDetailSrc("userAdd.html?branchId="+branchId + "&branchName=" + branchName.trim());
      },
      addDevice(branchId, branchName){
        setDetailSrc("deviceAdd.html?branchId="+branchId + "&branchName=" + branchName.trim());
      },
      grantDevice(deviceId, deviceName) {
        setDetailSrc("privilegeEdit.html?deviceId="+deviceId + "&deviceName=" + deviceName.trim());
      },
      grantUser(userId, nickName) {
        setDetailSrc("privilegeEdit.html?userId="+userId + "&nickName=" + nickName.trim());
      },
  }
});

function setDetailSrc(_target) {

    window.parent.setDetailSrc(_target);
}

// tree component

var data = {
  name: '管理单位 ',
  type:'root',
  open:true,
  children: [
    { name: '潘庄渠首管理所',
      open:false,
      type:'branch',
      id:'1',
      children: [
        {
          name:'人员',
          type:'branchUser',
          id:'1',
          children: [
            {
              name:'张三',
              type:'user',
              id:'1'
            },
            {
              name:'李四',
              type:'user',
              id:'5'

            }
          ]
        },
        {
          name:'设备',
          type:'branchDevice',
          id:'1',
          children: [
            {
              name:'1号水闸',
              type:'device',
              id:'1'
            },
            {
              name:'2号水闸',
              type:'device',
              id:'2'

            }
          ]
        },
      ]
    },
    { name: '齐河中心管理所',
      open:false,
      type:'branch',
      branchId:'2',
      children: [
        {
          name:'人员'
        },
        {
          name:'设备'
        },
      ]
    },
    { name: '禹城中心管理所',
      open:false,
      children: [
        {
          name:'人员'
        },
        {
          name:'设备'
        },
      ]
    },
    { name: '平原中心管理所',
      open:false,
      children: [
        {
          name:'人员'
        },
        {
          name:'设备'
        },
      ]
    },
  ]
}

// define the item component
Vue.component('item', {
  template: '#item-template',
  props: {
    model: Object
  },
  data: function () {
    return {
      open: (this.model.open && this.model.open==true) ?true:false
    }
  },
  computed: {
    isFolder: function () {
      return this.model.children &&
        this.model.children.length
    },
  },
  methods: {
    toggle: function () {
      if (this.isFolder) {
        this.open = !this.open
      }

    },

    showDetail: function() {
      if (this.model.type && this.model.type == 'branch') {
        setDetailSrc('branchDetail.html?branchId=' + this.model.id);
      }
      if (this.model.type && this.model.type == 'user') {
        setDetailSrc('userDetail.html?userId=' + this.model.id);
      }
      if (this.model.type && this.model.type == 'device') {
        setDetailSrc('deviceDetail.html?deviceId=' + this.model.id);
      }
    },

    changeType: function () {
      if (!this.isFolder) {
        Vue.set(this.model, 'children', [])
        this.addChild()
        this.open = true
      }
    },
    addChild: function () {
      this.model.children.push({
        name: 'new stuff'
      })
    },
  }
})


function makeMenu(_data) {
  var menu = new Vue({
    el: '#menu',
    data: {
      treeData: _data,
    }
  });
}


function initContextMenu() {
  $('.needContextMenu').each(
    function(index, element) {

      var arr = element.id.split("_");
      if (!arr) {
        return;
      }
      var type = arr[0];
      var id = arr[1];
      var tag = element.innerHTML;
      addContextMenu(element.id, getActions(type, id, tag));
  });
}

function addContextMenu(_id, _actions) {
  if (_actions.length == 0) {
    return;
  }

  var menuRight = new BootstrapMenu('#'+_id, {
    menuEvent: 'right-click',
    menuSource: 'element',
    menuPosition: 'belowLeft',
    actions: _actions,
  });

  $(".bootstrapMenu").css("width", "20px");
  $(".dropdown-menu").css("width", "20px");
}

function getActions(type, id, tag) {

    var actions = [];
    if (type=='root') {
      actions = [
        {
          name:'增加管理单位',
          onClick:function() {
            app.addBranch();
          }
        },
        {
          name:'刷新',
          onClick:function() {
            window.location.href=window.location.href;
          }
        }
      ];
    }

    if (type=='branch') {
      actions = [
        {
          name:'增加人员',
          onClick:function() {
            app.addUser(id, tag);
          },
        },
        {
          name:'增加设备',
          onClick:function() {
            app.addDevice(id, tag);
          },
        }
      ];
    }

    if (type=='user') {
      actions = [{
        name:'授权',
        onClick:function() {
          app.grantUser(id, tag);
        }
      }];
    }

    if (actions.length == 0) {

        actions = [{
          name:'-',
          onClick:function() {

          }
        }];
    }

    return actions;
}


function getData() {

    if (!checkAuth()) {
       return;
    }

    var param = {"method":"sysman.menu.get",
                "auth":[localStorage.secretId, localStorage.secretKey],
              };

    ajaxPost(G_API_URL, param,
      function(response){

        if (response.status<0) {
          layer.msg(response.msg, {icon:2,time:1000});
          return;
        }

        var branchList = response.result;

        var _menuData ={
          name: '管理单位 ',
          type:'root',
          open:true,
          children: []
        }

        for(i in branchList) {
          var br = branchList[i];
          var brMenu = {"name":br.branchName,
                      "type":"branch",
                      "id":br.branchId,
                      "open":false,
                      "children":[
                        {
                          "name":"人员",
                          "type":"branchUser",
                          "id":br.branchId,
                          "open":false,
                          "children":[],
                        },
                        {
                          "name":"设备",
                          "type":"branchDevice",
                          "id":br.branchId,
                          "open":false,
                          "children":[],
                        },

                      ],
                    };

          for(j in br.user) {
            var user = br.user[j];
            var userItem = {
              "name":user.nickName+'('+ user.account+')',
              "type":"user",
              "id":user.userId,
            }
            brMenu.children[0].children.push(userItem);
          }

          for(k in br.device) {
            var device = br.device[k];
            var deviceItem = {
              "name":device.deviceName+'('+ device.deviceSn+')',
              "type":"device",
              "id":device.deviceId,
            }
            brMenu.children[1].children.push(deviceItem);
          }

          _menuData.children.push(brMenu);
        }

        makeMenu(_menuData);
        initContextMenu();

    });
}
