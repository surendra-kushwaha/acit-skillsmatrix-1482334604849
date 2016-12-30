$(function() {
    /*$('.dataTables_length, .paging_full_numbers').hide();
    $(".selections .selector-multiple").attr("disabled", true);*/
    var pTable = $('#policyDetails').DataTable({
        "sPaginationType": "full_numbers",
        "iDisplayLength": 10,
        "sDom": 'T<"clear">lfrtip',
        "oTableTools": {
            "sRowSelect": "multi",
            "aButtons": [{
                "sExtends": "select_all",
                "sButtonText": "Select Filtered",
                "sSelectedClass": "row_selected",
                "fnClick": function(nButton, oConfig, oFlash) {
                    var oTT = TableTools.fnGetInstance('policyDetails');
                    oTT.fnSelectAll(true); //True = Select only filtered rows (true). Optional - default false.
                }
            }, {
                "sExtends": "select_none",
                "mColumns": "visible"
            }]
        },
        "aoColumnDefs": [{
            'bSortable': false,
            'aTargets': [0, 9, 10]
        }],
        "deferRender": true,
        "colVis": {
            exclude: [0]
        },
        "fnInitComplete": function(oSettings, json) {
            if ($('#policyDetails tbody tr td').hasClass("dataTables_empty")) {
                $('tfoot').hide();
            } else {
                $('tfoot').css("display", "table-header-group");
            }
        },
        "fnDrawCallback": function(oSettings) {
            $this = this;
            if ($('#policyDetails tbody tr td').hasClass("dataTables_empty")) {
                $(".dwn-btns, #selecctall").attr('disabled', true);
                $('.dataTables_length, .paging_full_numbers').hide();
            } else {
                $('.dataTables_length, .paging_full_numbers').show();
                $('tfoot').css("display", "table-header-group");
                $('#selecctall').prop("disabled", false);
                $("#expExcel").attr("disabled", false);
                if ($('input[name="formSel"]:checked').length > 0) {
                    $(".dwn-btns").prop("disabled", false);
                } else {
                    $(".dwn-btns").not('#expExcel').prop("disabled", true);
                }
            };
            $("#selecctall").on("change", function() {
                if ($(this).prop("checked")) {
                    $("#ToolTables_policyDetails_0").click();
                } else {
                    $("#ToolTables_policyDetails_1").click();
                }
                setTimeout(checkBoxCtrls(), 50);
            });

            $(".paginate_button").on("click", function() {
                checkBoxCtrls();
            });

            $(".text_filter").keyup(function(event) {
                checkBoxCtrls();
            });
            $('input[name="formSel"]').on("change", function() {
                if ($('input[name="formSel"]:checked').length > 0) {
                    $(".dwn-btns").prop("disabled", false);
                } else {
                    $(".dwn-btns").not('#expExcel').prop("disabled", true);
                }
            });

            $(".edit-box").on("click", function(e) {
                e.stopImmediatePropagation();
                var editval = {};
                editval = {
                    "formno": $(this).data("formno"),
                    "desc": $(this).data("desc"),
                    "btype": $(this).data("btype"),
                    "frmtype": $(this).data("ftype"),
                    "mantry": $(this).data("mand"),
                    "source": $(this).data("source"),
                    "states": $(this).data("state"),
                    "protfolio": $(this).data("prof"),
                    "lobs": $(this).data("lob")
                }
                editretainValues(editval, 1);
                $("#myModal").modal("show");
            });
            toolTips("titles", "js/portfolio.json");
            toolTips("state-titles", "js/portfolio.json");
        }
    }).columnFilter();

    /*$("#selecctall").change(function(){
         $(".checkboxs").prop('checked', $(this).prop("checked"));
    });*/

    $drop1 = $("#businessType");
    $drop2 = $('#lob');
    $drop2.append($('<option>').text('Select Business Type First').attr('value', ''));
    var drop1Value = $("#businessType").val();
    var lob = {
        "": "All LOB",
        "BP": "Business Owners",
        "CA": "Commercial Auto",
        "CR": "Crime & Fidelity",
        "GL": "General Liability",
        "CM": "Commercial Inland Marine",
        "CF": "Commercial Property",
        "IL": "Common",
        "EP": "Employment-Related Practices Liability",
    };
    //[ 'Business Owners', 'Commercial Auto', 'Crime & Fidelity', 'General Liability', 'Commercial Inland Marine', 'Commercial Property', 'Common', 'Employment-Related Practices Liability'];
    //dropdownAdd(lob);
    $drop1.change(function() {
        drop1Value = $(this).val();
        dropdownAdd(lob, drop1Value);
    });

    $('.data-btype').on("change", function() {
        drop1Value = $(this).val();
        dropdownAdd(lob, drop1Value, 3);
    });

    $("#searchbtn").click(function() {
        document.getElementById("searchForm").submit();
        $('#myTabs li a:first').click();
    });

    $('#myTabs a').click(function(e) {
        e.preventDefault();
        $pane = $("#" + $(this).attr('data-url'));
        $(".tabsContent").addClass("hide");
        $("#multipleUpload").click();
        $(this).parent("li").addClass("active").siblings("li").removeClass("active");
        $pane.removeClass("hide");
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

    $("#btnPdf").click(function() {
        $("#downloadForm").attr("action", "downLoadPdfFiles");
        var oTable = $('#policyDetails').dataTable();
        //var rowcollection =  oTable.$(".DTTT_selected .checkboxs:checked", {"page": "all"});
        var rowcollection = oTable.$("#formSel:checked", {
            "page": "all"
        });

        var formNos = [];
        rowcollection.each(function(index, elem) {
            var checkbox_value = $(elem).val();
            formNos.push("'" + checkbox_value + "'");
        });
        $('#downloadForm').prepend('<input type="hidden" name="selectedPdf" id="selectedPdf"/>');
        $('input[name="selectedPdf"]').val(formNos);
        formNos = [];
        $("#downloadForm").submit();
        $(this).blur();
    });

    $("#btnDoc").click(function() {
        $("#downloadForm").attr("action", "downLoadDocFiles");
        var oTable = $('#policyDetails').dataTable();
        var rowcollection = oTable.$("#formSel:checked", {
            "page": "all"
        });
        //var rowcollection =  oTable.$(".DTTT_selected .checkboxs:checked", {"page": "all"});
        var formNos = [];
        rowcollection.each(function(index, elem) {
            var checkbox_value = $(elem).val();
            formNos.push("'" + checkbox_value + "'");
        });
        $('#downloadForm').prepend('<input type="hidden" name="selectedDocs" id="selectedDocs"/>');
        $('input[name="selectedDocs"]').val(formNos);
        formNos = [];
        $("#downloadForm").submit();
        $(this).blur();
    });

    $("#expExcel").click(function() {
        $("#downloadForm").attr("action", "/excelReport.jsp");
        $("#downloadForm").submit();
        $(this).blur();
    });

    $.getJSON("js/usstates.json", function(data) {
        $.each(data, function(key, val) {
            $("#seleState").append($('<option value=' + key + '>').text(val));
        });
        //$('.dataTables_length, .paging_full_numbers').hide();
        setTimeout(retainValues(), 50);
        setTimeout(multiSele1(), 20);
    });

    var foo = {};
    var foo1 = {};
    $(".selector-multiple").change(function() {
        $selec = $(".selector-multiple");
        if ($("option:selected", this).length < 0) {
            $(".searched-items").hide();
        } else {
            //$(".searched-items").show();
        }
        $selectedContainer = $(this).parent().parent().next("div.searched-items").find("ul");
        console.log($selectedContainer);
        if ($(this).attr('id') != 'portfolio') {
            foo = {};
            $("option:selected", this).each(function(i, selected) {
                foo[$(selected).val()] = $(selected).text();
                $selectedContainer.children("li").remove();
            });
            $.each(foo, function(index, val) {
                $selectedContainer.append("<li><a id='clsItems' data-value='" + index + "' href='javascript:void(0)' class='resetdrp'>" + val + "&nbsp;[x]</a><li>");
            });
        } else {
            foo1 = {};
            $("option:selected", this).each(function(i, selected1) {
                foo1[$(selected1).val()] = $(selected1).text();
                $selectedContainer.children("li").remove();
            });

            $.each(foo1, function(index, val) {
                $selectedContainer.append("<li><a id='clsItems1' data-value='" + index + "' href='javascript:void(0)' class='resetdrp'>" + val + "&nbsp;[x]</a><li>");
            });
        }

    });
    $(".edit-box").on("click", function(e) {
        e.stopImmediatePropagation();
        var editval = {};
        editval = {
            "formno": $(this).data("formno"),
            "desc": $(this).data("desc"),
            "btype": $(this).data("btype"),
            "frmtype": $(this).data("ftype"),
            "mantry": $(this).data("mand"),
            "source": $(this).data("source"),
            "states": $(this).data("state"),
            "protfolio": $(this).data("prof"),
            "lobs": $(this).data("lob")
        }
        editretainValues(editval, 1);
        $("#myModal").modal("show");
    });

    $("#searchedItems").on("click", "#clsItems", function() {
        var vall = $(this).data("value");
        $('.selector-multiple option[value=' + vall + ']').prop('selected', false);
        $(this).remove();
        delete foo[vall];
    });

    $("#searchedItems1").on("click", "#clsItems1", function() {
        var vall = $(this).data("value");
        $('.selector-multiple option[value=' + vall + ']').prop('selected', false);
        $(this).remove();
        delete foo1[vall];
    });

    $(".resetdrp").on("click", function() {
        $(".searched-items ul li").remove();
        delete foo[vall];
    });

    $(".resetbtn").click(function(e) {alert("reset button called");
        $("form").find("input[type=text], input[type=password], textarea").val(null);
        $("form").find("select").selectedIndex = 0;
        e.preventDefault();
        return false;
    });
    //$(".dwn-btns").attr('disabled', true);
    setTimeout($("#myTabs li:first a:first").click(), 3000);
});

$(window).load(function() {
    var msg = "";
    var elements = $(".form-valid");
    console.log(elements);
    for (var i = 0; i < elements.length; i++) {
        elements[i].oninvalid = function(e) {
            e.target.setCustomValidity("");

            switch (e.target.id) {
                case "formNo1":
                    msg = "Please enter the form no.";
                    break;
                case "formdesc1":
                    msg = "Please enter the description";
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
        if ($("#docFile").val() != '' && $("#docFile").val().indexOf(".doc") != -1) {
            var myFilename = getPageName($("#" + $selectvalId).val());
            $("." + $selectvalId).val(myFilename);
            flags = 0;
        } else {
            flags = 1;
            $(".show-error").show();
            resetFile("docFile");
        }

        if ($("#zipFile").val() != '' && $("#zipFile").val().indexOf(".zip") != -1) {
            var myFilename = getPageName($("#" + $selectvalId).val());
            $("." + $selectvalId).val(myFilename);
            flags = 0;
        } else if ($("#zipFile").val() != '' && $("#zipFile").val().indexOf(".zip") == -1){
            flags = 1;
            //$(".show-error").show();
            alert("Please upload correct files");
            resetFile("zipFile");
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

    if (location.pathname.indexOf('uploadForm') != -1) {
        $(".success-msg").fadeIn();
        setTimeout($(".success-msg").fadeOut(), 15000);
    }
    if (location.pathname.indexOf('bulkUploadServlet') != -1) {
        $(".success-msg1").fadeIn();
        setTimeout($(".success-msg1").fadeOut(), 15000);
    }
    if (location.pathname.indexOf('editFormController') != -1) {
        if (updateErr == "updateSuccess") {
            $(".success-msg2").fadeIn();
            setTimeout($(".success-msg2").fadeOut(), 15000);
        }
    }

    $("input[type=text], textarea").bind("keypress", function(event) {
        if (event.charCode > 160 || !event.charCode) {
        	if(event.keyCode!=8 && event.keyCode!=9 && event.keyCode!=35 && event.keyCode!=36 && event.keyCode!=46 && event.keyCode!=13 && event.keyCode!=40 && event.keyCode!=39 && event.keyCode!=38 && event.keyCode!=37){
            alert("Warning: Contains unknown characters");
            event.preventDefault();
        	}
        }
    })

    $('input[type=text], textarea').bind('paste', function(e) {
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
    if (location.href.indexOf("policyAddForms") != -1) {
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

function showLoad1() {
    if ($(".add-multi-zip").val != '') {
        $(".loadImg").show();
        return true;
    } else {
        return false;
    }
}


function multiSele1() {
    /*var t = 0;
    $("#seleState :selected").each(function(index, value){
    	t++;
    });*/
    if ($("#sele-multi").val() == "N") {
        $(".selections .selector-multiple").attr("disabled", false);
    } else {
        $(".selections .selector-multiple").attr("disabled", true);
        $(".selections .selector-multiple option:selected").removeAttr("selected");
    }
}

function downLoadForm() {
    document.getElementById("downloadForm").submit();
}

function toolTips(attr, path) {
    $("." + attr).on("mouseover", function(e) {
        $this = $(this);
        $txt = [];
        $txtval = $(this).text();
        $arrayornot = $txtval.indexOf(",");
        if ($arrayornot != -1) {
            $txtValues = $txtval.split(", ");
            $.each($txtValues, function(i, vals) {
                $txt.push(vals);
            })
        } else {
            $txt.push($txtval);
        };
        if ($txt != "null" || $txt != '') {
            console.log($txt);
            $.getJSON(path, function(data) {
                $valArray = [];
                console.log();
                $.each(data, function(key, val) {
                    $.each($txt, function(k, v) {
                        if (v == key) {
                            $valArray.push(val);
                            $this.attr("title", $valArray);
                        }
                    });
                });
            });
        }
        e.preventDefault();
    });
}

function checkBoxCtrls() {
    $("#policyDetails tbody tr").each(function() {
        if ($(this).hasClass("DTTT_selected")) {
            $(this).children().find(".checkboxs").prop("checked", true);
        } else {
            $(this).children().find(".checkboxs").prop("checked", false);
        }
    })
}

function resetFile(id) {
    $("#" + id).replaceWith($("#" + id).val('').clone(true));
    $("." + id).val('');
};

function lStorage() {
    names = ["formno", "formtype", "bType", "mFroms", "lob", "sources", "multiState", "states"];
    $formNo = $("input[name=formNo]").val() != "" ? $("input[name=formNo]").val() : "";
    $formType = $("#formTypes").val() != "" ? $("#formTypes").val() : "";
    $bType = $("#businessType").val() != "" ? $("#businessType").val() : "";
    $mForms = $("#manditems").val() != "" ? $("#manditems").val() : "";
    $lob = $("#lob").val() != "" ? $("#lob").val() : "";
    $sources = $("#souritems").val() != "" ? $("#souritems").val() : "";
    $multiSt = $("#sele-multi").val() != "" ? $("#sele-multi").val() : "";
    $stats = $("#seleState").val() != "" ? $("#seleState").val() : "";
    values1 = [$formNo, $formType, $bType, $mForms, $lob, $sources, $multiSt, $stats];
    for (var i = 0; i < names.length; i++) {
        localStorage[names[i]] = values1[i];
    }
    return true;
}

function retainValues() {
    var isLoc = location.href.indexOf("SearchFormController");
    var formno3 = localStorage.getItem("formno");
    var frmType3 = localStorage.getItem("formtype");
    var btype3 = localStorage.getItem("bType");
    var mForms3 = localStorage.getItem("mFroms");
    var srces3 = localStorage.getItem("sources");
    var multsts = localStorage.getItem("multiState");
    var sts3 = localStorage.getItem("states");
    var lob3 = localStorage.getItem("lob");

    if (isLoc != -1) {
        $("input[name=formNo]").val(formno3);
        selectIds = ["formTypes", "businessType", "manditems", "lob", "souritems", "sele-multi", "seleState"];
        $("#formTypes option").each(function() {
            if ($(this).val() == frmType3 && frmType3 != "") {
                $("#formTypes option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
            /*
            	    if ($(this).val() == frmType3 && frmType3!="") {
            	    	 $("#formTypes option:contains("+$(this).val()+")").attr("selected", "selected");
            	    }*/
        })
        $("#businessType option").each(function() {
            if ($(this).val() == btype3 && btype3 != "") {
                $("#businessType option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })
        $("#manditems option").each(function() {
            if ($(this).val() == mForms3 && mForms3 != "") {
                $("#manditems option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })
        $("#souritems option").each(function() {
            if ($(this).val() == srces3 && srces3 != "") {
                $("#souritems option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })

        $("#sele-multi option").each(function() {
            if ($(this).val() == multsts && multsts != "") {
                $("#sele-multi option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })
        stte = sts3.split(",");
        $("#seleState option").each(function(i, val) {
            if (stte.indexOf($(this).val()) > -1) {
                $("#seleState option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            };
        })
        drop1Value = btype3;
        dropdownAdd(lob, drop1Value);

        $("#lob option").each(function(i, val) {
            if ($(this).val() == lob3 && lob3 != "") {
                $("#lob option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })
    } else {
        localStorage.clear();
    }
}

function editretainValues(values) {
    $("#fromnos").text(values.formno);
    $("#EditFormNo").val(values.formno);
    $(".data-desc").val($.trim(values.desc));
    selectIds = ["formTypes", "businessType", "manditems", "lob", "souritems", "sele-multi", "seleState"];
    $(".modal-content .data-formT option").each(function() {
        if ($(this).val() == values.frmtype) {
            $("#myModal .data-formT option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
        }
    })

    /*if(values.btype=='Commercial'){
    	valuee = 'btCl';
    }else if(values.btype=='Personal'){
    	valuee = 'btPer';
    }else if(values.btype=='Speciality'){
    	valuee = 'btSpec'
    }else{
    	valuee = ''
    }
    */
    $("#myModal .data-btype option").each(function() {
            if ($(this).val() == values.btype) {
                $("#myModal .data-btype option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })
        /*$("#myModal input[name=mandatory]").each(function(){
            if ($(this).val() == values.mantry) {
            	 $("#myModal #manditems option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })*/
    $("#myModal .data-source option").each(function() {
        if ($(this).val() == values.source) {
            $("#myModal .data-source option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
        }
    })

    $("#myModal input[name=mandatory]").each(function() {
        if ($(this).val() == values.mantry) {
            $(this).prop("checked", true);
        } else {
            $(this).prop("checked", false);
        }
    })
    if (values.protfolio != null) {
        prtf = [""];
        prtf = values.protfolio.split(", ");
    } else {
        prtf = [""];
    }
    //$("#myModal .data-prof option, #myModal .data-state option").remove();
    $("#myModal .data-prof option").prop("selected", false);
    $("#myModal .data-prof option").each(function() {
        if (prtf.indexOf($(this).val()) > -1) {
            $("#myModal .data-prof option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
        }
    })
    $("#myModal .data-state option").remove();
    $.getJSON("js/usstates.json", function(data) {
        $.each(data, function(key, val) {
            $("#myModal .data-state").append($('<option value=' + key + '>').text(val));
        });
        if (values.states != null) {
            stte1 = [""];
            stte1 = values.states.split(", ");
        } else {
            stte1 = [""];
        }

        $("#myModal .data-state option").each(function(i, val) {
            if (stte1.indexOf($(this).val()) > -1) {
                $("#myModal .data-state option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            };
        })
    });

    lobVlaue = values.lobs;
    drop1Value = values.btype;
    dropdownAdd(lob, drop1Value, 1);
}

function dropdownAdd(lob, drop1Value, mode) {
    switch (drop1Value) {
        case 'Commercial':
            lob = {
                "": "All LOB",
                "BP": "Business Owners",
                "CA": "Commercial Auto",
                "CR": "Crime & Fidelity",
                "GL": "General Liability",
                "CM": "Commercial Inland Marine",
                "CF": "Commercial Property",
                "IL": "Common",
                "EP": "Employment-Related Practices Liability",
            }
            appendLob(lob, mode);
            break;
        case 'Personal':
            lob = {
                "": "All LOB",
                "PA": "Personal Auto",
                "HO": "Homeowners",
                "PM": "Personal Inland Marine",
                "TR": "Travel",
                "DL": "Personal Liability",
                "MT": "Motorcycle"
            };
            appendLob(lob, mode);
            break;
        case 'Speciality':
            lob = {
                "": "All LOB",
                "MP": "Management Protection"
            };
            appendLob(lob, mode);
            break;
        case '':
            $('#lob option, .data-lob option').remove();
            $drop2.append($('<option>').text('Select Business Type First').attr('value', ''));
            break;
    }
}

function appendLob(lob, mode) {
    if (mode != 1 && mode != 3) {
        $('#lob option').remove();
        $drop2 = $('#lob');
        if (location.href.indexOf("policyAddForms")) {
            lob[''] = 'Select LOB'
        }
        $.each(lob, function(index, value) {
            $drop2.append($('<option>').text(value).attr('value', index));
            console.log($drop2);
        });
    } else {
        $('.data-lob option').remove();
        $drop2 = $('.data-lob');
        delete lob[''];
        $.each(lob, function(index, value) {
            $drop2.append($('<option>').text(value).attr('value', index));
            console.log($drop2);
        });
        $(".data-lob option").each(function(i, val) {
            if ($(this).val() == lobVlaue) {
                $(".data-lob option[value=\'" + $(this).val() + "\']").attr("selected", "selected");
            }
        })
    }
    drop1Value = '';
}