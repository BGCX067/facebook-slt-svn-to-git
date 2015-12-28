<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Suit Profile</title>
<link rel="stylesheet" type="text/css" href="/resources/profile.css"/>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

<script language="Javascript" type="text/javascript">
function load_small(address) {
	window.open(address,'','scrollbars=yes,menubar=no,height=1000,width=1140,resizable=yes,toolbar=no,location=no,status=no');
}
function gup( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return results[1];
}
function load(e,tag){
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
		document.getElementById(e).innerHTML=xmlHttp.responseText;
	}
	xmlHttp.open("GET",'/game/ajax_'+tag+'?gid='+gup('id'),true);
	xmlHttp.send(null); 
}
window.onload=function(){
}
</script>
</head>
<body>
<div class="container">
  <header>
    <wtext align="center">Suit Info</wtext>
  </header>
  <div class="sidebar1">
    <aside2>
    <p></p>
	</aside2>
    <nav>
      <ul>
        <li><a href="#" onClick="javascript:load(extra_graph_win);">Show Graph 1</a></li>
        <li><a href="#" onClick="javascript:load();">Show Graph 2</a></li>
      </ul>
    </nav>
    <aside2 id="ranking">
	</aside2>
	</div>
  <article class="content">
    <h1></h1>
    <section id="recent">
    </section>
    <section id="overall">
    </section>
    <section id="extra">
    </section>
  </article>
  <aside id="friends">
  Loading
  </aside>
  <footer>
    <wtext align="center">Copyright 2011 Alina, Chris, and Edison</wtext>
  </footer>
</div>
</body>
</html>
