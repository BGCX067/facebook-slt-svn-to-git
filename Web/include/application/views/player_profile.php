<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Suit Profile</title>
<link rel="stylesheet" type="text/css" href="/resources/profile.css"/>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<script src="/resources/jquery.js" type="text/javascript"></script>
<script src="/resources/highcharts.js" type="text/javascript"></script>

<script language="Javascript" type="text/javascript">
function load_small(address) {
	window.open(address,'','scrollbars=yes,menubar=no,height=1000,width=1140,resizable=yes,toolbar=no,location=no,status=no');
}
function load_card(address) {
	window.open(address,'','scrollbars=yes,menubar=no,height=600,width=540,resizable=yes,toolbar=no,location=no,status=no');
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
	xmlHttp.open("GET",'/player/ajax_'+tag+'?id='+gup('id'),true);
	xmlHttp.send(null); 
}
function load_chart1() {
	  var chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'extra',
		         defaultSeriesType: 'line',
		         marginRight: 130,
		         marginBottom: 25
		      },
		      title: {
		         text: 'Monthly Average Temperature',
		         x: -20 //center
		      },
		      subtitle: {
		         text: 'Source: WorldClimate.com',
		         x: -20
		      },
		      xAxis: {
		         categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 
		            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
		      },
		      yAxis: {
		         title: {
		            text: 'Temperature (°C)'
		         },
		         plotLines: [{
		            value: 0,
		            width: 1,
		            color: '#808080'
		         }]
		      },
		      tooltip: {
		         formatter: function() {
		                   return '<b>'+ this.series.name +'</b><br/>'+
		               this.x +': '+ this.y +'°C';
		         }
		      },
		      legend: {
		         layout: 'vertical',
		         align: 'right',
		         verticalAlign: 'top',
		         x: -10,
		         y: 100,
		         borderWidth: 0
		      },
		      series: [{
		         name: 'Tokyo',
		         data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
		      }, {
		         name: 'New York',
		         data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
		      }, {
		         name: 'Berlin',
		         data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
		      }, {
		         name: 'London',
		         data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
		      }]
		   });
}
window.onload=function(){
	load('ranking','ranking');
	load('recent','recent');
	load('overall','overall');
	load('friends','friends');
	load('played','played');
	load('speed','speed');
	load('extra','extra_frequent_suits');
	//load_chart1();
}
</script>
</head>
<body>
<div class="container">
  <header>
    <wtext align="center">Suit Profile</wtext>
  </header>
  <div class="sidebar1">
    <aside2>
    <p><img src="https://graph.facebook.com/<?=$profile['fid']?>/picture?type=large"/></p>
	</aside2>
    <nav>
      <ul>
        <li><a href="#" onClick="javascript:load('extra','extra_frequent_suits')">Most Frequent Suits</a></li>
       <!--  <li><a href="#" onClick="javascript:load_chart1();">Show Graph 2</a></li>
        <li><a href="#" onClick="javascript:load();">Show Graph 3</a></li>
        <li><a href="#" onClick="javascript:load();">Show Graph 4</a></li> -->
      </ul>
    </nav>
    <aside2 id="ranking">
  Loading
	</aside2>
    <aside2 id="speed">
  Loading
	</aside2>
	</div>
  <article class="content">
    <h1><a href="http://www.facebook.com/profile.php?id=<?=$profile['fid']?>" target="_blank"><?=$profile['name']?></a></h1>
    <section id="recent">
  Loading
    </section>
    <section id="overall">
  Loading
    </section>
    <section id="extra">
  Loading
    </section>
  </article>
  <aside id="friends">
  Loading
  </aside>
  <aside id="played">
  Loading
  </aside>
  <footer>
    <wtext align="center">Copyright 2011 Alina, Chris, and Edison</wtext>
  </footer>
</div>
</body>
</html>
