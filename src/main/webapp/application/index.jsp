<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>Application main</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

</head>
<style>
.modal-dialog {
	width: whatever
}

.modal-body {
	height: 500px; 
	max-height: 80vh;
}
</style>
<script>
var access_token=""
</script>
<body>

	<nav id="myNavbar"
		class="navbar navbar-default navbar-inverse navbar-fixed-top"
		role="navigation"> <!-- Brand and toggle get grouped for better mobile display -->
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#navbarCollapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/application">Demo Application</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="navbarCollapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="/application">Home</a></li>
				<li><a href="#myModal" data-toggle="modal"
					data-target="#myModal">Authorise</a></li>
			</ul>
		</div>
	</div>
	</nav>
	<div class="container">
		<div class="jumbotron">
			<h1>3rd party application</h1>
			<p>main 3rd party landing page</p>
		</div>
		<hr>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<div>
					<iframe height="400px" width="100%" height="100%" frameborder="0"
						src="http://127.0.0.1:8080/webapi/authorization.jsp?redirect_uri=http://127.0.0.1:8080/application"></iframe>
				</div>
			</div>
		</div>
	</div>

	<div class="container">

	<div class="row">
	<h1>apis</h1>
	
	 <div class="col-sm-3 col-md-6" style="background-color:lavender;">
			<button type="button" data-toggle="modal" id="button1">call	/api/resource1 please</button>
			</br>
			<button type="button" data-toggle="modal" id="button2">call	/api/resource2 please</button>
			</br>
			<button type="button" data-toggle="modal" id="button3">call	/api/resource3 please</button>
			</br>
			<p><small id="accessTokenText"></small></p>
			<div >
			<p id="apiresponse2"></p>
			<p id="errorHint"></p>
			</div>
			</div>
			</div>
	 </div>

</body>
<script>

function call(what) {
	 $.ajax( {
	    	url: 'http://127.0.0.1:8080/api/demo/'+what,
	    	type: 'GET',
	    	beforeSend : function( xhr ) {
	        	xhr.setRequestHeader( "Authorization", "Bearer " + access_token );
	    	},
	    	success: function( response ) {
	    		$('#apiresponse2').text("API response is "+response);
	       	},
	    	error: function( response, textStatus, errorThrown ) {
	    		$('#apiresponse2').text("Oh dear, API response is "+response.status+" "+response.getResponseHeader('WWW-Authenticate')); 
	    		$('#errorHint').text("refresh browser if access token expired, this will use token to get a new one. If application token expired then reauthorise a new one");  
	    	}
			} );
	 
	}
	$(function(){
	    $('#button1').on('click', function(e){
	      e.preventDefault(); // preventing default click action
		  call("1")
	    });	 
     });
	$(function(){
	    $('#button2').on('click', function(e){
	      e.preventDefault(); // preventing default click action
		  call("2")
	    });	 
     });
	$(function(){
	    $('#button3').on('click', function(e){
	      e.preventDefault(); // preventing default click action
		  call("3")
	    });	 
     });

  	$.urlParam = function(name) {
		var results = new RegExp('[\?&]' + name + '=([^&#]*)')
				.exec(window.location.href);
		if (results == null) {
			return null;
		} else {
			return results[1] || 0;
		}
	}

	var code = $.urlParam('code');
	if (code != null) {
		var encoded = "?grant_type=authorization_code\&code="
				+ code
				+ "&redirect_uri=http://127.0.0.1:9090/&client_id=oauth2test&client_secret=oauth2clientsecret";

		$.ajax({
			type : "POST",
			data : {
				tbd : "tbd"
			},
			url : "http://127.0.0.1:8080/api/token" + encoded,
			success : function(data) {
				access_token = data["access_token"]
				$('#accessTokenText').text("access token is "+access_token)
			},
			error: function( response ) {
				$('#accessTokenText').text( "Oh dear, access token failed "+response.status+" "+response.responseText);   
	    	}
		});
	}

</script>

</html>