
window.onload = function() {
  
}

var app = new Vue({
  el: '#app',
  data: {
    doorName:'潘庄渠首 1号闸门',
  },
  watch: {

  },
  methods:{

  }
});

function setDetailSrc(_target) {
  $("#if_detail").attr("src", _target);
}

function setMenuSrc(_target) {
  $("#if_menu").attr("src", _target);
}
