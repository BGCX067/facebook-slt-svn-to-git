<?php
define('pClass',APPPATH.'libraries/pChart/');
include(pClass."class/pData.class.php");
include(pClass."class/pDraw.class.php");
require_once(pClass."class/pImage.class.php");
//include('common.php');
class Avgtime extends CI_Controller {
	function __construct(){
		parent::__construct();
		$this->load->helper('graph'); 
	}
	function index(){
		
		$width = 600;
		$height = 230;
		/* Create and populate the pData object */
		$MyData = new pData();  
 		$MyData->addPoints(array(-4,VOID,VOID,12,8,3),"Female");
 		$MyData->addPoints(array(3,12,15,8,5,-5),"Male");
 		//$MyData->addPoints(array(2,0,5,18,19,22),"Probe 3");
 		$MyData->setSerieTicks("Male",4);
 		$MyData->setAxisName(0,"Number of males, females");
 		$MyData->addPoints(array("Jan","Feb","Mar","Apr","May","Jun"),"Labels");
 		$MyData->setSerieDescription("Labels","Months");
 		$MyData->setAbscissa("Labels");

 		/* Create the pChart object */
		$myPicture = new pImage($width,$height,$MyData);

 		/* Draw the background */
 		$Settings = array("R"=>170, "G"=>183, "B"=>87, "Dash"=>1, "DashR"=>190, "DashG"=>203, "DashB"=>107);
 		$myPicture->drawFilledRectangle(0,0,$width,$height,$Settings);

 		/* Overlay with a gradient */
 		$Settings = array("StartR"=>219, "StartG"=>231, "StartB"=>139, "EndR"=>1, "EndG"=>138, "EndB"=>68, "Alpha"=>50);
 		$myPicture->drawGradientArea(0,0,$width,$height,DIRECTION_VERTICAL,$Settings);
 		//$myPicture->drawGradientArea(0,0,700,20,DIRECTION_VERTICAL,array("StartR"=>0,"StartG"=>0,"StartB"=>0,"EndR"=>50,"EndG"=>50,"EndB"=>50,"Alpha"=>80));

 		/* Add a border to the picture */
 		$myPicture->drawRectangle(0,0,$width-1,$height-1,array("R"=>0,"G"=>0,"B"=>0));
 
		/* Write the picture title */ 
 		//$myPicture->setFontProperties(array("FontName"=>pClass."fonts/Silkscreen.ttf","FontSize"=>6));
 		//$myPicture->drawText(10,13,"drawBarChart() - draw a bar chart",array("R"=>255,"G"=>255,"B"=>255));

 		/* Write the chart title */ 
 		$myPicture->setFontProperties(array("FontName"=>pClass."fonts/Forgotte.ttf","FontSize"=>11));
 		$myPicture->drawText(250,55,"Average time to find a set",array("FontSize"=>20,"Align"=>TEXT_ALIGN_BOTTOMMIDDLE));

 		/* Draw the scale and the 1st chart */
 		$myPicture->setGraphArea(60,60,450,190);
 		$myPicture->drawFilledRectangle(60,60,450,190,array("R"=>255,"G"=>255,"B"=>255,"Surrounding"=>-200,"Alpha"=>10));
 		$myPicture->drawScale(array("DrawSubTicks"=>TRUE));
 		$myPicture->setShadow(TRUE,array("X"=>1,"Y"=>1,"R"=>0,"G"=>0,"B"=>0,"Alpha"=>10));
 		$myPicture->setFontProperties(array("FontName"=>pClass."fonts/pf_arma_five.ttf","FontSize"=>10));
 		$myPicture->drawBarChart(array("DisplayValues"=>TRUE,"DisplayColor"=>DISPLAY_AUTO,"Rounded"=>TRUE,"Surrounding"=>30));
 		//$myPicture->drawBarChart(array("DisplayValues"=>TRUE,"DisplayColor"=>DISPLAY_AUTO,"Surrounding"=>30));
 		$myPicture->setShadow(FALSE);

 		/* Draw the scale and the 2nd chart */
 		/*
 		$myPicture->setGraphArea(500,60,670,190);
 		$myPicture->drawFilledRectangle(500,60,670,190,array("R"=>255,"G"=>255,"B"=>255,"Surrounding"=>-200,"Alpha"=>10));
 		$myPicture->drawScale(array("Pos"=>SCALE_POS_TOPBOTTOM,"DrawSubTicks"=>TRUE));
 		$myPicture->setShadow(TRUE,array("X"=>1,"Y"=>1,"R"=>0,"G"=>0,"B"=>0,"Alpha"=>10));
 		$myPicture->drawBarChart();
 		$myPicture->setShadow(FALSE);
 		*/

 		/* Write the chart legend */
 		//$myPicture->drawLegend(510,205,array("Style"=>LEGEND_NOBORDER,"Mode"=>LEGEND_HORIZONTAL));
 		$myPicture->drawLegend(500,105,array("Style"=>LEGEND_NOBORDER,"Mode"=>LEGEND_VERTICAL));

 		/* Render the picture (choose the best way) */
 		$myPicture->stroke();
 	}

}
?>