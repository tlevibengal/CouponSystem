function Reset(form_id) {
    var elements = form_id.elements.Reset;
}


//function hide() {
//    document.getElementById('purchaiseResponse').style.display = 'none';
//    document.getElementById('buyResponseDiv').style.display = 'none';
//    document.getElementById('responseMainCoup').style.display = 'none';
//}
//To detect escape button



document.onkeydown = function (evt) {
    evt = evt || window.event;
    if (evt.keyCode == 27) {
        hide('popDiv');
    }
};


//PURCHAISE


$(document).ready(function () {
    $("#allCoupButton").click(function () {
        $("#coupContainer").fadeToggle();
    });
});

$(document).ready(function () {
    $("#buynowButton").click(function () {
        $("#mainReplyDiv").fadeToggle();
        $("#coupContainer").fadeOut();
    
    });
});

$(document).ready(function () {
    $("#againAllCoups").click(function () {
        $("#mainReplyDiv").fadeOut();
    });
});


$(document).ready(function () {
    $("#closeReply").click(function () {
        $("#mainReplyDiv").fadeOut();
    });
});

$(document).ready(function () {
    $("#closeReply2").click(function () {
        $("#coupContainer").fadeOut();
    });
});

$(document).ready(function () {
    $("#closeResponseAllPurchased").click(function () {
        $("#allpurchasedResponse").fadeOut();
        $("#mainReplyDiv").fadeOut();
    });
});


//

//all preveously purchased

$(document).ready(function () {
    $("#allpurchased ").click(function () {
        $("#allpurchasedResponse").fadeToggle();
    });
});


$(document).ready(function () {
    $("#logoutIcon").click(function () {
        window.location = "index.html";
    });
});

function pop(div) {
    document.getElementById(div).style.display = 'block';
}
function hide(div) {
    document.getElementById(div).style.display = 'none';
}