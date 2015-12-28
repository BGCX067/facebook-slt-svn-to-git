<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Aggregate Graphs</title>
<link rel="stylesheet" type="text/css" href="/resources/aggreg.css"/>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

<script src="/resources/jquery.js" type="text/javascript"></script>
<script src="/resources/highcharts.js" type="text/javascript"></script>

</head>
<body>
<div class="container">
  <header>
    <wtext align="center">Aggregate Graphs</wtext>
  </header>

  <article class="content">
  	<section id="gender">
  		<h4>Player Gender</h4>
		<p>Percentage, by gender, of people playing Suit.</p>

		<div id="gender-chart-container" style="width: 600px; height: 400px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'gender-chart-container',
		         plotBackgroundColor: null,
		         plotBorderWidth: null,
		         plotShadow: false
		      },
		      title: {
		         text: 'Gender Demographics'
		      },
		      tooltip: {
		         formatter: function() {
		            return '<b>'+ this.point.name +'</b>: '+ this.y +' %';
		         }
		      },
		      plotOptions: {
		         pie: {
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		               enabled: true,
		               color: '#000000',
		               connectorColor: '#000000',
		               formatter: function() {
		                  return '<b>'+ this.point.name +'</b>: '+ this.y +' %';
		               }
		            }
		         }
		      },
		       series: [{
		         type: 'pie',
		         name: 'Gender Distribution',
		         data: [
		                ['Female',   <?=$gender[0];?>],
		                ['Male',     <?=$gender[1];?>],
		         ]
		      }]
		   });
		});
		</script>
		</div>
  	</section> 
  	
  	<section id="rating_gender">
    	<h4>Ratings (Gender)</h4>
		<p>Number of males, females in each group of ratings</p>
		
		<div id="rating-gender-chart-container" style="width: 600px; height: 400px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'rating-gender-chart-container',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Ratings by Gender'
		      },
		      xAxis: {
		         categories: [<?=$ratinglabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of males, females'
		         }
		      },
		      legend: {
		         layout: 'vertical',
		         backgroundColor: '#FFFFFF',
		         align: 'left',
		         verticalAlign: 'top',
		         x: 100,
		         y: 70,
		         floating: true,
		         shadow: true
		      },
		      tooltip: {
		         formatter: function() {
			         if (this.y == 1)
				         return '' 
				         	+ 'rating in this range : ' + this.y + ' person';
			         else return '' 
			         		+ 'rating in this range: ' + this.y + ' people';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Female',
		         data: [<?=$femrating;?>]
		   
		      }, {
		         name: 'Male',
		         data: [<?=$malerating;?>]
		 
		      }]
		   });
		   
		   
		});
		</script>
		</div>
    </section>
  	
  	<section id="time_all">
  		<h4>Average Time</h4>

		<div id="time-chart-container" style="width: 600px; height: 400px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'time-chart-container',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Average Time to Find a Suit (seconds)'
		      },
		      xAxis: {
		         categories: [<?=$timelabels?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of people'
		         }
		      },
		      legend: {
			      enabled: false
		      },
		      tooltip: {
		         formatter: function() {
			         if (this.y == 1)
				         return '' 
				         	+ this.x + ' seconds: ' + this.y + ' person';
			         else return '' 
			         		+ this.x + ' seconds: ' + this.y + ' people';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'People',
		         data: [<?=$timedata?>]
		   
		      }]
		   });
		   
		   
		});
		</script>
		</div>
  	</section>
    <section id="time_gender">
    	<h4>Average Time (Gender)</h4>

		<div id="time-gender-chart-container" style="width: 600px; height: 400px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'time-gender-chart-container',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Average Time to Find a Suit, by Gender (seconds)'
		      },
		      xAxis: {
		         categories: [<?=$timelabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of males, females'
		         }
		      },
		      legend: {
		         layout: 'vertical',
		         backgroundColor: '#FFFFFF',
		         align: 'left',
		         verticalAlign: 'top',
		         x: 100,
		         y: 70,
		         floating: true,
		         shadow: true
		      },
		      tooltip: {
		         formatter: function() {
			         if (this.y == 1)
				         return '' 
				         	+ this.x + ' seconds: ' + this.y + ' person';
			         else return '' 
			         		+ this.x + ' seconds: ' + this.y + ' people';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Female',
		         data: [<?=$femtime;?>]
		   
		      }, {
		         name: 'Male',
		         data: [<?=$maletime;?>]
		 
		      }]
		   });
		   
		   
		});
		
		</script>
		</div>
    </section>
    
    
    <section id="suitattrib-time">
    	<h4>Time to Find Suits With a Certain Attribute</h4>
    	<p>What is the average time to find a suit in which all three cards 
    		are of the same color?</br> Shape? Fill? Number? </p> 
    
    	<div id="set-time-all-chart-container" style="float: left; width: 50%; height: 200px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-all-chart-container',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Attribute'
		      },
		      xAxis: {
		         categories: ['color', 'number', 'fill', 'shape']
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      legend: {
			      enabled: false
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Seconds',
		         color: '#DB843D',
		         data: [<?=$attribdata?>]
		   
		      }]
		   }); 
		});
    	</script>
    	</div>
    	
    	<div id="set-time-gen-chart-container" style="float:left; width: 50%; height: 200px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-gen-chart-container',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Attribute, by Gender'
		      },
		      xAxis: {
		         categories: ['color', 'number', 'fill', 'shape']
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      legend: {
			         layout: 'vertical',
			         backgroundColor: '#FFFFFF',
			         align: 'left',
			         verticalAlign: 'top',
			         x: 60,
			         y: 35,
			         floating: true,
			         shadow: true
			  },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Female',

		         data: [<?=$femattrib?>]
		   
		      },{
			     name: 'Male',
			     data: [<?=$maleattrib?>]
		      }]
		   }); 
		});
    	</script>
    	</div>
    
    </section>
   
    
    <section id="suittype_time">
    	<h4>Time to Find Suits With Specific Attribute Types</h4>
    	<p>What is the average time to find a suit in which all three cards 
    		are of the same specific color?</br> Shape? Fill? Number? </p>
    	<p>Attributes: color, shape, fill, number</br>
    		Types: red/green/blue (Color), square/circle/triangle (Shape), 
    		none/shaded/solid (Fill), one/two/three (Number)</p>
    	
    	<div id="set-time-chart-container" style="float:left; width: 25%; height: 200px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-chart-container',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Color'
		      },
		      xAxis: {
		         categories: [<?=$colorlabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      legend: {
			      enabled: false
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Seconds',
		         data: [<?=$colordata?>]
		   
		      }]
		   }); 
		});
    	</script>
    	</div>
    	
 
    	
    	<div id="set-time-chart-container2" style="float:left; width: 25%; height: 200px">    	
    	
    	<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-chart-container2',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Number'
		      },
		      xAxis: {
		         categories: [<?=$numberlabels?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      legend: {
			      enabled: false
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + '-suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Seconds',
		         color: '#AA4643',
		         data: [<?=$numberdata?>]
		   
		      }]
		   }); 
		});
    	</script>
    	</div>
    	
    	<div id="set-time-chart-container3" style="float:left; width: 25%; height: 200px">    	
    	
    	<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-chart-container3',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Fill'
		      },
		      xAxis: {
		         categories: [<?=$filllabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      legend: {
			      enabled: false
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Seconds',
		         color: '#89A54E',
		         data: [<?=$filldata?>]
		   
		      }]
		   }); 
		});
    	</script>
    	</div>
    	
    	<div id="set-time-chart-container4" style="float:left; width: 25%; height: 200px">    	
    	
    	<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-chart-container4',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Shape'
		      },
		      xAxis: {
		         categories: [<?=$shapelabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      legend: {
			      enabled: false
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Seconds',
		         color: '#80699B',
		         data: [<?=$shapedata?>]
		   
		      }]
		   }); 
		});
    	</script>
    	</div>
    	
    </section>
    
    <section id="suittype_time_gender">
    	<h4>Time to Find Suits With Specific Attribute Types (Gender)</h4>
    	
    	<div id="set-time-gender-chart-container" style="float:left; width: 25%; height: 235px">
		
		<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-gender-chart-container',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Color'
		      },
		      xAxis: {
		         categories: [<?=$colorlabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Female',
		         data: [<?=$femcolor?>]
		   
		      },{
			     name: 'Male',
			     data: [<?=$malecolor?>]

		      }]
		   }); 
		});
    	</script>
    	</div>
    	
 
    	
    	<div id="set-time-gender-chart-container2" style="float:left; width: 25%; height: 235px">    	
    	
    	<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-gender-chart-container2',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Number'
		      },
		      xAxis: {
		         categories: [<?=$numberlabels?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + '-suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Female',
		         data: [<?=$femnumber?>]
		   
		      },{
			     name: 'Male',
			     data: [<?=$malenumber?>]

		      }]
		   }); 
		});
    	</script>
    	</div>
    	
    	<div id="set-time-gender-chart-container3" style="float:left; width: 25%; height: 235px">    	
    	
    	<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-gender-chart-container3',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Fill'
		      },
		      xAxis: {
		         categories: [<?=$filllabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Female',
		         data: [<?=$femfill?>]
		   
		      },{
				 name: 'Male',
				 data: [<?=$malefill?>]

			  }]
		   }); 
		});
    	</script>
    	</div>
    	
    	<div id="set-time-gender-chart-container4" style="float:left; width: 25%; height: 235px">    	
    	
    	<script type="text/javascript">
		var chart;
		$(document).ready(function() {
		   chart = new Highcharts.Chart({
		      chart: {
		         renderTo: 'set-time-gender-chart-container4',
		         defaultSeriesType: 'column'
		      },
		      title: {
		         text: 'Shape'
		      },
		      xAxis: {
		         categories: [<?=$shapelabels;?>]
		      },
		      yAxis: {
		         min: 0,
		         title: {
		            text: 'Number of seconds'
		         }
		      },
		      tooltip: {
		         formatter: function() {
		        	 return '' 
			         	+ this.x + ' suit: ' + this.y + ' seconds';
		         }
		      },
		      plotOptions: {
		         column: {
		            pointPadding: 0.2,
		            borderWidth: 0
		         }
		      },
		           series: [{
		         name: 'Female',
		         data: [<?=$femshape?>]
		   
		      },{
				 name: 'Male',
				 data: [<?=$maleshape?>]

			  }]
		   }); 
		});
    	</script>
    	</div>
    	
    </section>

  </article>
  <footer>
    <wtext align="center">Copyright 2011 Alina, Chris, and Edison</wtext>
  </footer>
</div>
</body>
</html>