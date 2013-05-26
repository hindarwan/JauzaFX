document.querySelectorAll('[jfxid="id"]').item(0);
var a = document.querySelector('[jfxid="32"]').getAttribute('class');
document.querySelector('[jfxid="32"]').
        setAttribute('class', a + ' sg_selected');

document.querySelectorAll('[class*=\\\"sg_selected\\\"]')