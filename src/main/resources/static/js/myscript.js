/* Add your custom Javascrit here */

/*jQuery(document).ready(function($) {
              var referrer = document.referrer;
                console.log(referrer);
                if (referrer == "http://localhost:5000/manage") {
                    setTimeout(function() {
                        $('#addcust').modal('show');
                    }, 5000);
                }
        });*/

/*$(document).ready(function() {

			$(".nav-tabs a").click(function() {
				$(this).tab('show');
			});
		});*/
/*$(document).ready(function(){
    $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
        localStorage.setItem('activeTab', $(e.target).attr('href'));
    });
    var activeTab = localStorage.getItem('activeTab');
    if(activeTab){
        $('#home a[href="' + activeTab + '"]').tab('show');
    }
});*/

$(document).ready(function() {
	   var activeTab = localStorage.getItem('activeTab');
	   if(activeTab){
	   $('.nav-tabs a[href="' + activeTab + '"]').tab('show');
	   }
	$('.tabs .nav-tabs a').on('click', function(e)  {
	        var currentAttrValue = jQuery(this).attr('href');
	        localStorage.setItem('activeTab', currentAttrValue);
	        jQuery('.tabs ' + currentAttrValue).siblings().slideUp(400);
	        jQuery('.tabs ' + currentAttrValue).delay(400).slideDown(400);  
	        jQuery(this).parent('li').addClass('active').siblings().removeClass('active');       
	        e.preventDefault();
	        });
	});


function loadEWDates() {
	 // alert("Image is loaded");2018-06-12T19:30
	 var today = new Date();
	 // alert("Image is loaded"+today);
	 var dd = today.getDate();
	 var mm = today.getMonth()+1; //January is 0!
	 var yyyy = today.getFullYear();
	 var backhour=today.getHours()-1;
	 var min=today.getMinutes();
	 var currenthour=today.getHours();
	
	  if(dd<10){
	         dd='0'+dd
	     } 
	     if(mm<10){
	         mm='0'+mm
	     } 
	     if(backhour<10){
	    	 backhour='0'+backhour
	     } 
	     if(min<10){
	    	 min='0'+min
	     } 
	     if(currenthour<10){
	    	 currenthour='0'+currenthour
	     } 

	 var oneHourLate = yyyy+'-'+mm+'-'+dd+'T'+backhour+':'+min;
	 var currentTime = yyyy+'-'+mm+'-'+dd+'T'+currenthour+':'+min;
	 document.getElementById("startDate").setAttribute("max",oneHourLate );
	 document.getElementById("endDate").setAttribute("max",currentTime );
	 document.getElementById("startDate").setAttribute("value", oneHourLate);
	 document.getElementById("endDate").setAttribute("value",currentTime );
	 document.getElementById("startDateO").setAttribute("max",oneHourLate );
	 document.getElementById("endDateO").setAttribute("max",currentTime );
	 document.getElementById("startDateO").setAttribute("value", oneHourLate);
	 document.getElementById("endDateO").setAttribute("value",currentTime );
	 document.getElementById("startDateI").setAttribute("max",oneHourLate );
	 document.getElementById("endDateI").setAttribute("max",currentTime );
	 document.getElementById("startDateI").setAttribute("value", oneHourLate);
	 document.getElementById("endDateI").setAttribute("value",currentTime );
	}

function finalInvoice()
{
	var element= document.getElementById("invoiceButton");
	  element.classList.remove("hidden");
	var element=  document.getElementById("editButton");
	  element.classList.remove("hidden");
	 document.getElementById("addLine").classList.add("hidden");
	 document.getElementById("saveButton").classList.add("hidden");
	 document.getElementById("additionallines").readOnly=true;
	// $('#lineInputDesc').attr("style", "pointer-events: none;");
	
	 if(document.getElementsByName('lineInput')[0]){
	//making all lines as readonly
		 let inputs = document.getElementsByName('lineInput');
		 for (let input of inputs) {
				input.readOnly=true;
				//input.attr("style", "pointer-events: none;");
		   }
	//disable all remove line button
	 let buttons = document.getElementsByName('removeL');
	 for (let button of buttons) {
	 	button.style.visibility = 'hidden';
	   }
	// calculate line total
	
	 var lineQty=$("[id^=lineInputQty]");
	 var lineRate=$("[id^=lineInputRate]");
	 var lineTotal=$("[id^=lineInputTotal]");
	 var lineDesc=$("[id^=lineInputDesc]");
	 for (var i = 0, length = lineQty.length; i < length; i++) {
		 lineTotal[i].value=lineQty[i].value*lineRate[i].value;
		 lineDesc[i].disabled = true;
		 
	 }
	 
	 
	 
	 }
}
function editInvoice()
{
	 var element = document.getElementById("addLine");
	  element.classList.remove("hidden");
	  var element = document.getElementById("saveButton");
	  element.classList.remove("hidden");

	 document.getElementById("invoiceButton").classList.add("hidden");
	 document.getElementById("editButton").classList.add("hidden");
	 document.getElementById("additionallines").readOnly=false;
	// $('#lineInputDesc').attr("style", "pointer-events: auto;");
	
	 if(document.getElementsByName('lineInput')[0]){
			 let inputs = document.getElementsByName('lineInput');
			 for (let input of inputs) {
			 	input.readOnly=false;
			
			   }
			
		 let buttons = document.getElementsByName('removeL');
		 for (let button of buttons) {
		 	button.style.visibility = 'visible';
		   }
		 var lineDesc=$("[id^=lineInputDesc]");
		 for (var i = 0, length = lineDesc.length; i < length; i++) {
			
			 lineDesc[i].disabled = false;
		 }
		 }
}
function saveInvoice()
{

	 jsonObj = [];
	    $("[name=lineInput]").each(function() {

	        var id = $(this).attr("id");
	        var value = $(this).val();
	        line = {}
	        line [id] = value;
	       // line ["value"] = value;
	        jsonObj.push(line);
	    });
	    $("[placeholder=Idno]").each(function() {

	        var id = $(this).attr("name");
	        var value = $(this).val();
	        line = {}
	        line [id] =value;
	        //line ["value"] = value;
	        jsonObj.push(line);
	    });
	    
	  
	   // console.log(JSON.stringify(jsonObj));
	
	

   $.ajax({type: "POST",url: "http://localhost:5000/invoice/createi",data:JSON.stringify(jsonObj),  success: function(result){
      //  $("#invoicebody").html(result);
	   var printdata = document.getElementById('invoicebody');
	    newwin = window.open("");
	    newwin.document.write(result);//data: printdata.outerHTML 
	    newwin.print();
	    newwin.close();
  }});
   
	
   
	
}
function addFields()
{
		var value = parseInt(document.getElementById('additionallines').value, 10);
	
	    value = isNaN(value) ? 0 : value;
	    value++;
	    document.getElementById('additionallines').value = value;
	 /*   
	  <input  id='lineInputDesc"+value+"'  type='text'  class='form-group col-xs-4' name='lineInput' placeholder='LineDetail'></input>*/
document.getElementById("addLine").insertAdjacentHTML("afterend", 
"  <div class='row' id='row"+value+"'><div " +
		"class='form-group col-xs-12'><label id='line_count' name='line_count' class='form-group col-xs-1'>Line</label><select class='form-group col-xs-4' name='lineInput' placeholder='LineDetail' id='lineInputDesc"+value+"'><option value='DryClean'>DryClean</option>"
+"<option value='OnlyIron'>OnlyIron</option><option value='CompleteWash'>CompleteWash</option> <option value='CurtainWash'>CurtainWash</option></select>"+
     " <input  id='lineInputQty"+value+"' value='0' type='number'  class='form-group col-xs-2'  name='lineInput' placeholder='Qty'></input><input  id='lineInputRate"+value+"' type='number'  value='0' class='form-group col-xs-2' name='lineInput' placeholder='Rate'><input  id='lineInputTotal"+value+"' type='number'  class='form-group col-xs-2' name='lineInput' placeholder='Total'></input><input id='removeL' name='removeL' type='button'  onclick=\"removeFields(this);\"  value='X'></input></div></div>	 "); 

}
function removeFields(o)
{
	var value=  document.getElementById('additionallines').value;
	 value--;
	document.getElementById('additionallines').value=value;
var p=o.parentNode.parentNode;
p.parentNode.removeChild(p);
}


function showModal(data)
{
	var client = new HttpClient();
	client.get('http://localhost:5000/invoice/createInvoice/'+data, function(response) {
	   
	});
	 $("#editI .modal-title").html(data)
	   $("#editI").modal();
}
function showMyModalSetTitle(id, otherID) {

		 $.ajax({url: "http://localhost:5000/invoice/getCidOid/"+otherID+"/"+id, success: function(result){
	             $("#invoicebody").html(result);
	            $("#editI").modal('show'); 
	          
	        }});
	}
	    
	    function othername() {
    var input = document.getElementById("userInput").value;
    alert(input);
}
	    
	    $("#addCModalForm").submit(function( event ) {
	        $("#AddCButton").prop("disabled", true).addClass("disabled");
	    });
	    
		
	 	  $("#del-user").click(function() {
	 		 var checked = $("#user-data-section input:checked").length > 0;
	 		    if (!checked){
	 		        alert("Please check at least one checkbox");
	 		        return false;
	 		    }
	 		    
	 		    $.ajax({
	 		        url: "http://localhost:5000/customer/all"
	 		    }).then(function(data) {
	 		       $('.greeting-id').append(data.ab_Cidno);
	 		       $('.greeting-content').append(data.ab_Ckey);
	 		    });
	 		    $('.mdb-select').materialSelect();
	 		    
	 		    $('.nav-tabs a').on('shown.bs.tab', function(event){
	 		        var x = $(event.target).text();         // active tab
	 		        var y = $(event.relatedTarget).text();  // previous tab
	 		        $(".act span").text(x);
	 		        $(".prev span").text(y);
	 		      }); 
	   
	 });
	 	 function removeExpenseFields(o)
	 	{
	 		var value=  document.getElementById('additionallines').value;
	 		 value--;
	 		document.getElementById('additionallines').value=value;
	 	var p=o.parentNode.parentNode;
	 	p.parentNode.removeChild(p);
	 	}
	 	 
	 	
	 	function showInvoices(id) {
	 		console.log(id);
	 		var validateID = id;
	 		if (validateID == '') {
	 			alert("Enter Order ID.");
	 		}
			 $.ajax({url: "http://localhost:5000/invoice/getOid/"+id, success: function(result){
		             $("#invoiceTotal").html(result);
		            $("#totalI").modal('show'); 
		          
		        }});
		}
	 	function sendInvoiceEmail(id)
	 	{
	 		
	 		 $.ajax({url: "http://localhost:5000/invoice/bill/"+id, success: function(result){
	          //   $("#invoiceTotal").html(result);
	       	  if(result=='true')
	 				 alert("Email Send...");
	 			  else
	 				 alert("Not able to send Email...");
	         $("#totalI").modal("hide"); 
		 		 
	          
	        }});
	 	}
	 	
	 	function viewDetailInvoice(id) {

			 $.ajax({url: "http://localhost:5000/invoice/getIid/"+id, success: function(result){
		             $("#invoiceDetail").html(result);
		            $("#editi").modal('show'); 
		          
		        }});
		}
	 	function downloadInvoiceEmail(id)
	 	{
	 		
	 		// $.ajax({url: "http://localhost:5000/invoice/billdownload/"+id, success: function(result){
	 			window.open("http://localhost:5000/invoice/billdownload/"+id);
	 		         $("#totalI").modal("hide"); 
	      //  }});
	 	}
	
	 	
	 	 
	 
	 	  
	   



