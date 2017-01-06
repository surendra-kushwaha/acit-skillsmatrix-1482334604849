$(function() {
    $('.data-btype').on("change", function() {});

    $("#searchbtn").click(function() {
        document.getElementById("searchForm").submit();
        $('#myTabs li a:first').click();
    });
    
    $("#reset").click(function(){//alert('#reset clicked');
        $(":input","#searchForm")
        .not(":button, :submit, :reset, :hidden")
        .val("")
        .removeAttr("selected");
    });
       
    if (errormsgs == "exists") {
        $("#singleUpload").click();
        $(".multipleUpload").addClass("hide");
        $(".singleUpload").removeClass("hide");
    }

    $(".formNos").click(function() {
        $seleId = $(this).attr("id");
        $(".formsTb").addClass("hide");
        $("." + $seleId).removeClass("hide");
    });
    $(".hiddenElement").click(function() {
        $selectval = $(this).attr("id");
        $("." + $selectval).click();
    });

    var foo = {};
    $(".resetdrp").on("click", function() {
        $(".searched-items ul li").remove();
        delete foo[vall];
    });
    
    $("#expExcel").click(function() {
        $("#downloadForm").attr("action", "/excelReport.jsp");
        $("#downloadForm").submit();
        $(this).blur();
    });

    $(".resetbtn").click(function(e) {//alert("reset buttn called@@");
        $("searchForm").find("input[name='enterprizeId'], input[name='expertSkills'], input[name='supSkills']").val(null);        
        e.preventDefault();
        return false;
    });
    //$(".dwn-btns").attr('disabled', true);
    setTimeout($("#myTabs li:first a:first").click(), 3000);
    
	    //$( "#datepicker" ).datepicker({  maxDate: 0 });
		  /*$( "#datepicker" ).datepicker({
			  dateFormat: 'mm/dd/yy',
			  maxDate: 0
			  //numberOfMonths: 2
			}).attr('readonly', 'true').
			keypress(function(event){
			  if(event.keyCode == 8){
			    event.preventDefault();
			  }
			});*/
});

$(window).load(function() {
    var msg = "";
    var elements = $(".form-valid");    
    console.log(elements);
    for (var i = 0; i < elements.length; i++) {
        elements[i].oninvalid = function(e) {
            e.target.setCustomValidity("");

            switch (e.target.id) {
                case "formScore":
                    msg = "Please enter the score.";
                    break;
                case "Score1":
                    msg = "Please enter the score for section1.";
                    break;
                case "Score2":
                    msg = "Please enter the score for section2.";
                    break;
                case "Score3":
                    msg = "Please enter the score for section3.";
                    break;
                case "Score4":
                    msg = "Please enter the score for section4.";
                    break;
                case "Score5":
                    msg = "Please enter the score for section5.";
                    break;
                case "Score6":
                    msg = "Please enter the score for section6.";
                    break;
                case "cName":
                    msg = "Please enter the certification name";
                    break;
                case "datepicker":
                    msg = "Please select certification date";
                    break;
            }                        

            if (!e.target.validity.valid) {
                e.target.setCustomValidity(msg);
            }
        };
        elements[i].oninput = function(e) {
            e.target.setCustomValidity("");
        };
    }       
    
    $(document).on("keypress", "form", function(event) { 
        return event.keyCode != 13;
    });
    
    $( "#score-box" ).click(function(e) {
  	  //alert( "Handler for .click() called." );
  	  //alert("main");
    	e.stopImmediatePropagation();
  	    $("#section1").text($(this).data("section1"));
        $("#section2").text($(this).data("section2"));
        $("#section3").text($(this).data("section3"));
        $("#section4").text($(this).data("section4"));
        $("#section5").text($(this).data("section5"));
        $("#section6").text($(this).data("section6"));
        $("#overall").text($(this).data("overall"));
        $("#myModal").modal("show");
        
  });
    
    $("#expExcel").click(function() {
        $("#downloadForm").attr("action", "/excelReport.jsp");
        $("#downloadForm").submit();
        $(this).blur();
    });
    
    $("#addReset").click(function(){
	    $(":input","#addForm")
	    .not(":button, :submit, :reset, :hidden, #enterprizeId, #employeeId, #skillRole, #workLocation")
	    .val("")
	    .removeAttr("selected");
    });
    
    /*$("#addReset").click(function(e) {
        $("#addForm").find("input[name='enterprizeId'], input[name='skillRole']").val(null);        
        e.preventDefault();
        return false;
    });*/
    
    $("input[type=file]").on('change', function() {
        $selectvalId = $(this).attr("id");
        console.log($selectvalId);
        var flags = 1;
        if ($("#pdfFile").val() != '' && $("#pdfFile").val().indexOf(".pdf") != -1) {
            var myFilename = getPageName($("#" + $selectvalId).val());
            $("." + $selectvalId).val(myFilename);
            flags = 0;
        } else {
            flags = 1;
            $(".show-error").show();
            resetFile("pdfFile");
        }
               
        if ($("#pdfFile").val() != '' || $("#docFile").val() != '') {
            $(".show-error").hide();
        }
    });

    function getPageName(s) {
        var sp = s.split('\\');
        var file = sp[sp.length - 1];
        return file;
    }

    if (location.pathname.indexOf('uploadCertificate') != -1) {
        $(".success-msg").fadeIn();
        setTimeout($(".success-msg").fadeOut(), 15000);
    }
    
    $( "#formScore" ).change(function() {
		if(parseInt($("#formScore").val())>100){
    		//$("#formScore").val('100');            		
    		$(".show-score-error").show();
    	}else{
    		$(".show-score-error").hide();
    	}
	});
    
    $( "#Score1" ).change(function() {
		if(parseInt($("#Score1").val())>100){
    		//$("#formScore").val('100');            		
    		$(".show-score1-error").show();
    	}else{
    		$(".show-score1-error").hide();
    	}
	});
    
    $( "#Score2" ).change(function() {
		if(parseInt($("#Score2").val())>100){
    		//$("#formScore").val('100');            		
    		$(".show-score1-error").show();
    	}else{
    		$(".show-score1-error").hide();
    	}
	});
    $( "#Score3" ).change(function() {
		if(parseInt($("#Score3").val())>100){
    		//$("#formScore").val('100');            		
    		$(".show-score1-error").show();
    	}else{
    		$(".show-score1-error").hide();
    	}
	});
    
    $( "#Score4" ).change(function() {
		if(parseInt($("#Score4").val())>100){
    		//$("#formScore").val('100');            		
    		$(".show-score1-error").show();
    	}else{
    		$(".show-score1-error").hide();
    	}
	});
    $( "#Score5" ).change(function() {
		if(parseInt($("#Score5").val())>100){
    		//$("#formScore").val('100');            		
    		$(".show-score1-error").show();
    	}else{
    		$(".show-score1-error").hide();
    	}
	});
    
    $( "#Score6" ).change(function() {
		if(parseInt($("#Score6").val())>100){
    		//$("#formScore").val('100');            		
    		$(".show-score1-error").show();
    	}else{
    		$(".show-score1-error").hide();
    	}
	});
    
    $("#formScore").bind("keypress", function(event) {//alert('hi');
	   var key = event.charCode || event.keyCode || 0;
		if(key!=8 && key!=9 && key!=35 && key!=36 && key!=46 && key!=13 && key!=40 && key!=39 && key!=38 && key!=37){
    		if(key<48 || key>57){
            //alert("Warning: Enter the score between 0 to 100");
            event.preventDefault();
        	}
		}   	
    });
    $("#Score1").bind("keypress", function(event) {//alert('hi');        
    	var key = event.charCode || event.keyCode || 0;
		if(key!=8 && key!=9 && key!=35 && key!=36 && key!=46 && key!=13 && key!=40 && key!=39 && key!=38 && key!=37){
    		if(key<48 || key>57){
            //alert("Warning: Enter the score between 0 to 100");
            event.preventDefault();
        	}
		}  	
    });
    $("#Score2").bind("keypress", function(event) {//alert('hi');        
    	var key = event.charCode || event.keyCode || 0;
		if(key!=8 && key!=9 && key!=35 && key!=36 && key!=46 && key!=13 && key!=40 && key!=39 && key!=38 && key!=37){
    		if(key<48 || key>57){
            //alert("Warning: Enter the score between 0 to 100");
            event.preventDefault();
        	}
		}   	
    });
    $("#Score3").bind("keypress", function(event) {//alert('hi');        
    	var key = event.charCode || event.keyCode || 0;
		if(key!=8 && key!=9 && key!=35 && key!=36 && key!=46 && key!=13 && key!=40 && key!=39 && key!=38 && key!=37){
    		if(key<48 || key>57){
            //alert("Warning: Enter the score between 0 to 100");
            event.preventDefault();
        	}
		}   	
	});
	$("#Score4").bind("keypress", function(event) {//alert('hi');        
		var key = event.charCode || event.keyCode || 0;
		if(key!=8 && key!=9 && key!=35 && key!=36 && key!=46 && key!=13 && key!=40 && key!=39 && key!=38 && key!=37){
    		if(key<48 || key>57){
            //alert("Warning: Enter the score between 0 to 100");
            event.preventDefault();
        	}
		}   	
	});
	$("#Score5").bind("keypress", function(event) {//alert('hi');        
		var key = event.charCode || event.keyCode || 0;
		if(key!=8 && key!=9 && key!=35 && key!=36 && key!=46 && key!=13 && key!=40 && key!=39 && key!=38 && key!=37){
    		if(key<48 || key>57){
            //alert("Warning: Enter the score between 0 to 100");
            event.preventDefault();
        	}
		}    	
	});
	$("#Score6").bind("keypress", function(event) {//alert('hi');        
		var key = event.charCode || event.keyCode || 0;
		if(key!=8 && key!=9 && key!=35 && key!=36 && key!=46 && key!=13 && key!=40 && key!=39 && key!=38 && key!=37){
    		if(key<48 || key>57){
            //alert("Warning: Enter the score between 0 to 100");
            event.preventDefault();
        	}
		} 	
	});
    
    $('input[type=text], #formScore').bind('paste', function(e) {
        e.preventDefault();
        var text = (e.originalEvent || e).clipboardData.getData('text/plain') || prompt('Paste something..');
        if (/^[\x00-\x7F]*$/.test(text)) {
            window.document.execCommand('insertText', false, text);
        } else {
            alert("Warning: Contains unknown characters");
        }
    });

});

function showLoad() {
	if($("#pdfFileText").val()==''){
    	//alert("Please select certificate to upload");
		$(".show-error").show();
    	return false;
    }
	if(parseInt($("#formScore").val())>100){
		//$("#formScore").val('100');            		
		$(".show-score-error").show();
		return false;
	}
	if(parseInt($("#Score1").val())>100 ||parseInt($("#Score2").val())>100||parseInt($("#Score3").val())>100
			||parseInt($("#Score4").val())>100||parseInt($("#Score5").val())>100||parseInt($("#Score6").val())>100){
		//$("#formScore").val('100');            		
		$(".show-score1-error").show();
		return false;
	}
    if (location.href.indexOf("skillAddForm") != -1) {
        if ($("#pdfFile").val() != '' || $("#docFile").val() != '') {
            $(".loadImg").show();
            return true;
        } else {
            $(".docFile, .addPdf, .pdfFile").addClass('error-mark');
            $(".show-error").show();
            return false;
        }
    } else {
        return true;
    }
}


function downLoadForm() {
    document.getElementById("downloadForm").submit();
}

function resetFile(id) {
    $("#" + id).replaceWith($("#" + id).val('').clone(true));
    $("." + id).val('');
};

/*$(function() {
    $( "#datepicker" ).datepicker({  maxDate: 0,dateFormat: 'dd/mm/yyyy' });
  });
$("#formNo1").validate({
	  rules: {
	    percentage: {
	      required: true,
	      range: [0, 100]
	    }
	  }
}); */