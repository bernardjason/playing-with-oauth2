<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <title>API SERVICE</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>


</head>

<body>

 <nav id="myNavbar" class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
        
    </nav>
    <div class="container">
        <div class="jumbotron">
            <h1>API SERVICE</h1>
			<p>Happy to proceed?</p>
				<div id="okresources" class="checkbox">
			
  				<label><input type="checkbox" id="okresources1" value="">Resource1</label>
				</div>
				<div class="checkbox">
				  <label><input type="checkbox" id="okresources2" value="">Resource2</label>
				</div>
				<div class="checkbox disabled">
				  <label><input type="checkbox" id="okresources3" value="">Resource3</label>
				</div>
   

        </div>
        <hr>
       <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" id="close">Close</button>
        <button type="button" class="btn btn-primary" id="save">Save changes</button>
      </div> 
    </div>
    
<script type="text/javascript">


	$.urlParam = function(name) {
		var results = new RegExp('[\?&]' + name + '=([^&#]*)')
				.exec(window.location.href);
		if (results == null) {
			return null;
		} else {
			return results[1] || 0;
		}
	}

	var redirect_uri = $.urlParam('redirect_uri');

	$(document).ready(function() {
		$("#save").click(function() {
			var perm=
				"resource1"+$('#okresources1').prop('checked')+" "+
				"resource2"+$('#okresources2').prop('checked')+" "+
				"resource3"+$('#okresources3').prop('checked')
			
			window.top.location.href = "http://127.0.0.1:8080/api/auth?redirect_uri="
					+ redirect_uri
					+ "&response_type=code&client_id=oauth2test"+"&scope="+perm;
		});
	});
	$(document).ready(function() {
		$("#close").click(function() {
			window.top.location.href =  redirect_uri
		});
	});
</script>
   
</body>

</html>