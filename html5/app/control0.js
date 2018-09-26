
var C_RIGHT_ARROW = "▶︎";
var C_DOWN_ARROW = "▼";


var app = new Vue({
  el: '#app',
  data: {
    openHeight:5,
    oldHeight:0,
    stopFlg:false,
  },
  methods:{
    showDetail() {
        var arrow = $("#btnShowDetail").text();
        if (arrow== C_RIGHT_ARROW) {
          $("#btnShowDetail").text(C_DOWN_ARROW);
          $("#detailInfo").css("display","block");
          $("#realtimeInfo").css("height","220");
        } else {
          $("#btnShowDetail").text(C_RIGHT_ARROW);
          $("#detailInfo").css("display","none");
          $("#realtimeInfo").css("height","110");
        }
    },
    moveDoor() {
      app.stopFlg = false;
      moveDoor(app.openHeight);
    },
    upDoor() {
      app.stopFlg = false;
      upDoor();
    },
    downDoor() {
      app.stopFlg = false;
      downDoor();
    },
    stopMove() {
        app.stopFlg = true;
    },
    oninput(e) {
      this.val=e.target.value.replace(/[^\d|.]/g,'');
      if (this.val > 5) {
        this.val = 5;
      }

      app.openHeight = this.val;
      console.log('app.openHeight',app.openHeight);
    }
  }
});

app.openHeight = app.oldHeight;
function upDoor() {
  var v = (app.openHeight*10+1)/10;
  console.log("v", v);
  if (v > 5 || app.stopFlg) {
    return;
  }
  app.openHeight = Math.floor(v * 10) / 10;
  console.log("app.openHeight", app.openHeight);
  moveDoor(app.openHeight);
  setTimeout("upDoor()", 1100);
}

function downDoor() {
  var v = (app.openHeight*10-1)/10;
  console.log("v", v);
  if (v < 0 || app.stopFlg) {
    return;
  }
  app.openHeight = Math.floor(v * 10) / 10;
  console.log("app.openHeight", app.openHeight);
  moveDoor(app.openHeight);
  setTimeout("downDoor()", 1100);
}
