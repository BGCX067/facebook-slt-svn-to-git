<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" type="text/css" href="/resources/style.css" id="forum_css" />
    <script src="https://www.google.com/jsapi?key=ABQIAAAArFMVFvQjD-W4aWwv1GYEjhSetPC2Lfk7GmLJn7y1OSAkSXcZvRTvjUd_xssuarnTrQ3dcqvYiK1EAw" type="text/javascript"></script>
    <script language="Javascript" type="text/javascript">
    //<![CDATA[
    google.load("jquery", "1");
    //]]>
    </script>
    <script type="text/javascript">
function load(address) {
	window.open(address,'','scrollbars=no,menubar=no,height=800,width=1200,resizable=yes,toolbar=no,location=no,status=no');
}
function load_small(address) {
	window.open(address,'','scrollbars=yes,menubar=no,height=1000,width=1140,resizable=yes,toolbar=no,location=no,status=no');
}
function refresh(address){
var xmlHttp;
	try{	
		xmlHttp=new XMLHttpRequest();// Firefox, Opera 8.0+, Safari
	}catch (e){
		try{
			xmlHttp=new ActiveXObject("Msxml2.XMLHTTP"); // Internet Explorer
		}catch (e){
		    try{
				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
			}catch (e){
				alert("No AJAX!?");
				return false;
			}
		}
	}
	xmlHttp.onreadystatechange=function(){
		document.getElementById('main-content').innerHTML=xmlHttp.responseText;
		//setTimeout('refresh()',20000);
	}
	xmlHttp.open("GET","/game_list/load?mode="+address,true);
	xmlHttp.send(null); 
}
window.onload=function(){
	refresh('joinable');
}
//-->
</script>
</head>
<body>
<header>
<section>
<a href="#" onClick="javascript:load_small('/aggregate');" class="ediclass">Data</a>
<a href="#" onClick="javascript:load_small('/player');" class="ediclass">My Profile</a>
<a href="#" onClick="javascript:refresh('joinable');" class="ediclass">Refresh Joinable Games</a> 
<a href="#" onClick="javascript:refresh('friends');" class="ediclass">Joinable with Friends</a>
<a href="#" onClick="javascript:refresh('started');" class="ediclass">Started Games</a>
<a href="#" onClick="javascript:refresh('ended');" class="ediclass">Ended Games</a>
</section>
</header>
<div id="main-content">
</div>
<p></p>
<div id="fb-root"></div><script src="http://connect.facebook.net/en_US/all.js#appId=<?=$appId?>&amp;xfbml=1"></script><fb:live-stream event_app_id="<?=$appId?>" width="600" height="500" xid="game_list" always_post_to_friends="false" align="center"></fb:live-stream> 
</body>