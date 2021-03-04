 function onSubmit(token) {
	
     document.getElementById("status-form").submit();
     
   }
 function loadImage() {
	 // alert("Image is loaded");2018-06-12T19:30
/*	 var today = new Date();
	 // alert("Image is loaded"+today);
	 var dd = today.getDate();
	 var mm = today.getMonth()+1; //January is 0!
	 var yyyy = today.getFullYear();
	 var time=today.getHours() + ":" + today.getMinutes()
	  if(dd<10){
	         dd='0'+dd
	     } 
	     if(mm<10){
	         mm='0'+mm
	     } 

	 var maxdate = yyyy+'-'+mm+'-'+dd+'T'+time;
	 var mindate = yyyy+'-'+mm+'-'+today.getDate()+'T'+time;
	 document.getElementById("expirationDate").setAttribute("max", maxdate);
	 document.getElementById("expirationDate").setAttribute("min", mindate);
	 */
	 // alert("Image is loaded");2018-06-12T19:30
	 var today = new Date();
	 // alert("Image is loaded"+today);
	 var dd = today.getDate();
	 var sevendd = today.getDate()+7;
	 var mm = today.getMonth()+1; //January is 0!
	 var yyyy = today.getFullYear();
	 var backhour=today.getHours()+2;
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
	     if(sevendd<10)
	    	 {
	    	 sevendd='0'+sevendd
	    	 
	    	 }
	     if(sevendd>30)
    	 {
    	 sevendd=sevendd%30
    	 
    	 }

	 var oneHouradvance = yyyy+'-'+mm+'-'+dd+'T'+backhour+':'+min;
	 var currentTime = yyyy+'-'+mm+'-'+dd+'T'+currenthour+':'+min;
	 var sevendayadvance = yyyy+'-'+mm+'-'+sevendd+'T'+currenthour+':'+min;
	 document.getElementById("expirationDate").setAttribute("max",sevendayadvance );
	 document.getElementById("expirationDate").setAttribute("min",oneHouradvance );
	 document.getElementById("expirationDate").setAttribute("value", oneHouradvance);
	
	}
 
 //////////////////////////////////////////////////////////////////////////////////////////////////
 var CaptchaCallback = function() {
     grecaptcha.render('RecaptchaField1', {'sitekey' : '6LfK6KcZAAAAAERfMdCIUd9ePqxvXORduTJlcv_-'});
     grecaptcha.render('RecaptchaField2', {'sitekey' : '6LcVShsaAAAAAFHc6bWOWcaa1anhHijfU7lBPYFW'});
 };
 /////////////////////////////////////////////////////////////////////////////////////////////
 var today = new Date();
 var dd = today.getDate();
 var mm = today.getMonth()+1; //January is 0!
 var yyyy = today.getFullYear();
  if(dd<10){
         dd='0'+dd
     } 
     if(mm<10){
         mm='0'+mm
     } 

 today = yyyy+'-'+mm+'-'+dd;
 document.getElementById("expirationDate").setAttribute("min", today);
