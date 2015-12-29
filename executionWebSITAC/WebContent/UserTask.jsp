<%@page import="executionWebSITAC.Engine"%>
<%@page import="executionWebSITAC.ServiceAttribute"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<% 

String serviceResult = (String) request.getAttribute("ServiceResult");

String taskExecutionId = (String) request.getAttribute("TaskExecutionId");

String serviceName = Engine.getCurrentService().getName();

List<ServiceAttribute> serviceAttributes = Engine.getCurrentService().getInputParams();

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

    <title><%=serviceName%></title>

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

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>

<script>

var map;
var marker;

function initialize() {
  
	var myLatlng = {lat: 39.4690455, lng: -0.371879}; //Valencia
	
	var mapOptions = {
    	zoom: 12,
    	disableDoubleClickZoom: true,
    	draggingCursor: 'default',
    	center: myLatlng
	};
	
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	
	map.addListener('click', function(e) {
		placeMarkerAndPanTo(e.latLng);
	});
	
}

function placeMarkerAndPanTo(latLng) {
	if(!marker) {
		marker = new google.maps.Marker({
			position: latLng,
		    map: map
		});
	}
	else {
		marker.setPosition(latLng);
	}
	
	document.getElementById('latHidden').value = latLng.lat().toFixed(5);
	document.getElementById('lngHidden').value = latLng.lng().toFixed(5);
}


</script>



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
<h3> <%=serviceName%> </h3>
</div>

<%
if(serviceAttributes.size() > 0) {
%>

<div class="container">

<br>

<form action="ServiceExecution" class="form-horizontal" role="form">

<%
for(ServiceAttribute at : serviceAttributes) {
	if(at.getTakesValueFrom() == null) {
		String type = at.getType();
		if(type.equals("string")) {
			
%>

<div class="form-group">
<label class="control-label col-sm-2"><%=at.getName()%>:</label>
<div class="col-sm-10">
<input type="text" name="<%=at.getId()%>" class="form-control">
</div>
</div>

<%	
}

if(type.equals("int")) {
%>

<div class="form-group">
<label class="control-label col-sm-2"><%=at.getName()%>:</label>
<div class="col-sm-10">
<input type="number" name="<%=at.getId()%>" class="form-control">
</div>
</div>

<%
}
if(type.equals("float")) {
%>

<div class="form-group">
<label class="control-label col-sm-2"><%=at.getName()%>:</label>
<div class="col-sm-10">
<input type="number" name="<%=at.getId()%>" class="form-control" step="any">
</div>
</div>

<%
}
if(type.equals("boolean")) {
%>

<div class="form-group">
<div class="col-sm-10">
<div class="checkbox">
    <label>
      <input type="checkbox" name="<%=at.getId()%>"> <%=at.getName()%>
    </label>
</div>
</div>
</div>

<%
}
if(type.equals("singleChoice")) {
%>

<div class="form-group">
<label class="control-label col-sm-2"><%=at.getName()%>:</label>
<div class="col-sm-10">
<select class="form-control" name="<%=at.getId()%>">

<%
Map<String,String> options = at.getOptions();
for(String key : options.keySet())
{
	String value = options.get(key);

%>

  <option value="<%=key%>"><%=value%></option>

<%
}

%>

</select>
</div>
</div>

<%
}
if(type.equals("multipleChoice")) {
%>

<div class="form-group">
<label class="control-label col-sm-2"><%=at.getName()%>:</label>
<div class="col-sm-10">
<select multiple class="form-control" name="<%=at.getId()%>">

<%
Map<String,String> options = at.getOptions();
for(String key : options.keySet())
{
	String value = options.get(key);

%>

  <option value="<%=key%>"><%=value%></option>

<%
}

%>

</select>
</div>
</div>

<%
}
if(type.equals("latLon")) {
	
	//TODO: Add support for more than one attribute of type "latLon"
	//Should it be possible to indicate the values of all the attributes in the same map?
	
%>

<div class="form-group">
<label class="control-label col-sm-2"><%=at.getName()%>:</label>
<div class="col-sm-10">
<div id="map-canvas" style="height:300px; width:500px"></div>
</div>
</div>

<input type="hidden" id="latHidden" name="<%=at.getId() + "Lat"%>">
<input type="hidden" id="lngHidden" name="<%=at.getId() + "Lng"%>">

<script>
initialize();
</script>

<%
}
}
}
%>

<div class="form-group"> 
<div class="col-sm-offset-2 col-sm-10">

<input type="submit" value="Cancel" class="btn btn-default" name="cancelButton">
<input type="submit" value="Next" class="btn btn-default" name="nextButton">

</div>
</div>

<input type="hidden" name="taskExecutionId" value="<%=taskExecutionId%>" />

</form>

</div>

<%
}
%>

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