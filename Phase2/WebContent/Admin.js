

function Reset(form_id) {
    var elements = form_id.elements.Reset;
}


function Do() {
    $('#cr-choice').fadeOut();
    $("#createCompForm").fadeOut();
    $("#createCustDiv").fadeOut();
    $('#up-choice').fadeOut();
    $("#updateCompForm").fadeOut();
    $("#updateCustForm").fadeOut();
    $('#dl-choice').fadeOut();
    $("#DELCompForm").fadeOut();
    $("#DELCustForm").fadeOut();
    $('#find-choice').fadeOut();
    $("#FINDCompForm").fadeOut();
    $("#FINDCustForm").fadeOut();
    $('#all-choice').fadeOut();
    $("#allCompForm").fadeOut();
    $("#allCustForm").fadeOut();
    $('#responseMainComp').fadeOut();
    $('#responseMainCust').fadeOut();
    $('#reply').fadeOut();
    $('#reply2').fadeOut();
    $('#reply3').fadeOut();
    $('#reply4').fadeOut();
    $('#reply5').fadeOut();
    $('#reply6').fadeOut();
    $('#reply7').fadeOut();
    $('#reply8').fadeOut();

}

document.onkeydown = function (evt) {
    evt = evt || window.event;
    if (evt.keyCode == 27) {
        hide('popDiv');
    }
};



$(document).ready(function () {
$('#Create-Comp').click(function () {
$('#reply').fadeToggle();	
});
});

$(document).ready(function () {
$('#Create-Cust').click(function () {
$('#reply2').fadeToggle();	
});
});

$(document).ready(function () {
	$('#UPComp').click(function () {
	$('#reply3').fadeToggle();	
	});
	});

	$(document).ready(function () {
	$('#UPCust').click(function () {
	$('#reply4').fadeToggle();	
	});
	});

	
	$(document).ready(function () {
		$('#DeleteComp').click(function () {
		$('#reply5').fadeToggle();	
		});
		});

		$(document).ready(function () {
		$('#DeleteCust').click(function () {
		$('#reply6').fadeToggle();	
		});
		});
	
	
	
		$(document).ready(function () {
			$('#FIND-COMP').click(function () {
			$('#reply7').fadeToggle();	
			});
			});

			$(document).ready(function () {
			$('#FIND-CUST').click(function () {
			$('#reply8').fadeToggle();	
			});
			});
	
	
			
	
	
//CREATE
$(document).ready(function () {
    $('#cr-choice').fadeOut();
    $("#create").click(function () {
        $('#cr-choice').fadeToggle();
    });
    
});

$(document).ready(function () {
    $("#createCompBtn").click(function () {
        $("#createCompForm").fadeToggle();
    });
});

$(document).ready(function () {
    $("#createCustBtn").click(function () {
        $("#createCustDiv").fadeToggle();
    });
});


//UPDATE comp

$(document).ready(function () {
    $('#up-choice').fadeOut();
    $("#UPDATEComp").click(function () {
            $('#up-choice').fadeToggle();
    });
});


$(document).ready(function () {
    $("#updateCompBtn").click(function () {
        $("#updateCompForm").fadeToggle();
    });
});


$(document).ready(function () {
    $("#updateCustBtn").click(function () {
        $("#updateCustForm").fadeToggle();
    });
});


//DELETE comp
$(document).ready(function () {
    $('#dl-choice').fadeOut();
    $("#DELComp").click(function () {
        $('#dl-choice').fadeToggle();
    });
});


$(document).ready(function () {
    $("#deleteCompBtn").click(function () {
        $("#DELCompForm").fadeToggle();
    });
});

$(document).ready(function () {
    $("#deleteCustBtn").click(function () {
        $("#DELCustForm").fadeToggle();
    });
});




//FIND 
$(document).ready(function () {
    $('#find-choice').fadeOut();
    $("#FindComp").click(function () {
        $('#find-choice').fadeToggle();
    });
});


$(document).ready(function () {
    $("#findCompBtn").click(function () {
        $("#FINDCompForm").fadeToggle();
    });
});

$(document).ready(function () {
    $("#findCustBtn").click(function () {
        $("#FINDCustForm").fadeToggle();
    });
});


//
//FIND all
$(document).ready(function () {
    $('#all-choice').fadeOut();
    $("#FindAll").click(function () {
        $('#all-choice').fadeToggle();

    });
});


$(document).ready(function () {
    $("#allCompBtn").click(function () {
        $("#responseMainComp").fadeToggle();
    });
});

$(document).ready(function () {
    $("#allCustBtn").click(function () {
        $("#responseMainCust").fadeToggle();
    });
});



//all task deleted
$(document).ready(function () {
    $("#task").click(function () {
        $("#taskResponse").fadeIn();
    });
});


//all task deleted
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

function pop(div) {
    document.getElementById(div).style.display = 'block';
}
function hide(div) {
    document.getElementById(div).style.display = 'none';
}
//To detect escape button

document.onkeydown = function (evt) {
    evt = evt || window.event;
    if (evt.keyCode == 27) {
        hide('popDiv');
    }
};
   
