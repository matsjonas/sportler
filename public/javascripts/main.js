function updateClock() {
    var today=new Date();
    var h=today.getHours();
    var m=today.getMinutes();
    var s=today.getSeconds();
    m = padNumber(m);
    s = padNumber(s);
    $('.navbar .clock > .time').html(h+":"+m+":"+s);
    setTimeout(function(){updateClock()},500);
}

function padNumber(i) {
    if (i < 10) {
        i = "0" + i
    }
    return i;
}

$(function() {
    updateClock();
});