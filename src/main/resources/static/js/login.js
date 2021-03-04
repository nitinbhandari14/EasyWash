var attempt = 3; // Variable to count number of attempts.
// Below function Executes on click of login button.
function validateAttempts(){
var username = document.getElementById("username").value;
var password = document.getElementById("password").value;

attempt --;// Decrementing by one.
if( attempt == 0){
document.getElementById("username").disabled = true;
document.getElementById("password").disabled = true;
document.getElementById("submit").disabled = true;
return false;

}
}
function validateFields()
{
	 var username = document.getElementById("username").value; 
	 var password = document.getElementById("password").value;
	 if (username==null || username=="")
	 { 
	 alert("Username cannot be blank"); 
	 return false; 
	 }
	 else if(password==null || password=="")
	 { 
	 alert("Password cannot be blank"); 
	 return false; 
	 } 
	}
