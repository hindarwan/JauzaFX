document.querySelectorAll('[jfxid="id"]').item(0);
var a = document.querySelector('[jfxid="32"]').getAttribute('class');
document.querySelector('[jfxid="32"]').
        setAttribute('class', a + ' sg_selected');


node = document.querySelectorAll('[class*=\\\"sg_selected\\\"]');

var node = document.getElementsByClassName('sg_selected');

ELEMENT.classList.remove("sg_selected");

var nodesArray = Array.prototype.slice.call(document.querySelectorAll("div"));
