<%@page import="executionWebSITAC.Engine"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<% 

Object obj = request.getAttribute("ServiceResult");
String serviceResult = "";

if(obj != null && obj instanceof String) {
	 serviceResult = (String) obj;
}

%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SITAC Execution Tool</title>

    <!-- Bootstrap core CSS -->
    <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="./bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="./bootstrap/css/theme.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="./bootstrap/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body role="document">

    <!-- Fixed navbar -->
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">SITAC</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="home.html">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">My Apps <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="proc1.html">Book Library</a></li>
              </ul>
            </li>
          </ul>            
          <form class="navbar-form navbar-right">
            <div class="form-group">
              <input type="search" placeholder="Search" class="form-control">
            </div>
            <button type="submit" class="btn btn-success">Search</button>
          </form>            
        </div><!--/.nav-collapse -->
      </div>
    </nav>

	<div class="container">

	<% 
	
	String successfulExec = (String) request.getAttribute("success");
	if(successfulExec != null) {
	
	%>
	
	
	<div class="container">

	<h2><%=Engine.getProcessName()%> has been successfully executed!</h2>

	</div>
	
	<%
	}
	%>
	
	<br>
	
	<form action="StartProcessExecution">
		<input type="submit" value="Start execution" class="btn btn-default">
	</form>

	</div>
	
<%
if(!serviceResult.equals("")) {
%>

<script type="text/javascript">
  alert("<%=serviceResult%>");
</script>

<%
}
%>	

  </body>
</html>

