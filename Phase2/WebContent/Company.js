function doResize() {
    document.getElementById('purchaiseForm').style.height = '182px';

}



function Reset(form_id) {
    var elements = form_id.elements.Reset;
}


function Do() {
	 $('.res').fadeOut();
    $('#responseMainCoup').fadeOut();
    $("#createForm").fadeOut();
    $("#createResponse").fadeOut();
    $("#UP1Response").fadeOut();
    $("#updateResponse").fadeOut();
    $("#DELForm").fadeOut();
    $("#FINDForm").fadeOut();
    $("#FINDResponse").fadeOut();
    $('#responseMainComp').fadeOut();
    $('#reply').fadeOut();
 

}
function pop(div) {
    document.getElementById(div).style.display = 'block';
}
function hide(div) {
    document.getElementById(div).style.display = 'none';
}


document.onkeydown = function (evt) {
    evt = evt || window.event;
    if (evt.keyCode == 27) {
        hide('popDiv');
    }
};


//PURCHAISE
$(document).ready(function () {
    $("#create1").click(function () {
        $("#createForm").fadeToggle();
        $("#createResponse").fadeOut();

    });
});



$(document).ready(function () {
    $("#create").click(function () {
        $("#createResponse").fadeIn();
        $("#createForm").fadeOut();

    });
});





//UPDATE

$(document).ready(function () {
    $("#UPDATE").click(function () {
        $("#UP1Response").fadeToggle();
    });
});


$(document).ready(function () {
    $("#UP").click(function () {
        $("#updateResponse").fadeIn();
    });
});



//DELETE
$(document).ready(function () {
    $("#DEL").click(function () {
        $("#DELForm").fadeToggle();
        $("#DELResponse").fadeOut();

    });
});



$(document).ready(function () {
    $("#DeleteC").click(function () {
        $("#DELResponse").fadeIn();
    });
});


//FIND
$(document).ready(function () {
    $("#FINDBUTTON").click(function () {
        $("#FINDForm").fadeToggle();
        $("#FINDResponse").fadeOut();

    });
});



$(document).ready(function () {
    $("#FIND").click(function () {
        $("#FINDResponse").fadeIn();
    });
});


//FIND ALL COUPS
$(document).ready(function () {
    $("#FindALL").click(function () {
        $("#ALLForm").fadeToggle();
        $("#ALLResponse").fadeOut();

    });
});



$(document).ready(function () {
    $("#FindALL").click(function () {
        $("#responseMainCoup").fadeIn();
    });
});

//NOT IN USE!!
//var cnt = 0;
//function CountFun() {
//    cnt = parseInt(cnt) + parseInt(1);
//    var divData = document.getElementById("all");
//    alert(cnt)
//}



$(function () {
    //  Accordion Panels
    $(".accordion div").hide();
    setTimeout("$('.accordion div').slideToggle('slow');", 1000);
    $(".accordion h3").click(function () {
        $(this).next(".pane").slideToggle("slow").siblings(".pane:visible").slideUp("slow");
        $(this).toggleClass("current");
        $(this).siblings("h3").removeClass("current");

    });
});













$(document).ready(function () {
    $("#send").click(function () {
        $("#P-Res").fadeIn();
    });
});


function getCouponType() {
    var e = document.getElementById("coupTypes");
    var value = e.options[e.selectedIndex].value;
    switch (value) {
        case 1:
            CouponType = (coupons.Beans.CouponType.FOOD);
            break;
        case 2:
            CouponType = (coupons.Beans.CouponType.HOME);
            break;

        case 3:
            CouponType = (coupons.Beans.CouponType.SPORTS);

            break;
        case 4:
            CouponType = (coupons.Beans.CouponType.TRAVEL);

            break;
        case 5:
            CouponType = (coupons.Beans.CouponType.BEAUTY);
            break;
        case 6:
            CouponType = (coupons.Beans.CouponType.GAMES);

            break;
        case 7:
            CouponType = (coupons.Beans.CouponType.FASHION);
            break;
        case 8:
            CouponType = (coupons.Beans.CouponType.ELECTRONICS);
            break;

        default:
            alert("you must choose a coupon type")
            break;
    }
    return CouponType;
}


$(document).ready(function () {
    $("#quit").click(function () {
        window.location = "index.html";
    });
});


$(document).ready(function () {
    $("#logoutIcon").click(function () {
        window.location = "index.html";
    });
});


function Reset(form_id) {
    var elements = form_id.elements.Reset;
}

