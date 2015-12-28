<?php
define('pClass',APPPATH.'libraries/pChart/');
include(pClass."class/pData.class.php");
include(pClass."class/pDraw.class.php");
include(pClass."class/pPie.class.php");
require_once(pClass."class/pImage.class.php");
//include('common.php');
class User_gender extends CI_Controller {
	function __construct(){
		parent::__construct();
		$this->load->helper('graph'); 
	}
	function index(){
		/* Create and populate the pData object */
 		$MyData = new pData();
 		$MyData->addPoints(getGenders($this->db),"ScoreA");  
 		$MyData->setSerieDescription("ScoreA","Application A");
 		/* Define the absissa serie */
 		$MyData->addPoints(array("Female","Male"),"Labels");
 		$MyData->setAbscissa("Labels");	
 		/* Create the pChart object */
 		$myPicture = new pImage(240,180,$MyData,TRUE);
 		/* Set the default font properties */ 
 		$myPicture->setFontProperties(array("FontName"=>pClass."fonts/Forgotte.ttf","FontSize"=>10,"R"=>80,"G"=>80,"B"=>80));
 		/* Create the pPie object */ 
 		$PieChart = new pPie($myPicture,$MyData);
 		/* Enable shadow computing */ 
 		$myPicture->setShadow(TRUE,array("X"=>3,"Y"=>3,"R"=>0,"G"=>0,"B"=>0,"Alpha"=>10));
 		/* Draw a splitted pie chart */ 
 		$PieChart->draw3DPie(120,90,array("Radius"=>100,"DataGapAngle"=>12,"DataGapRadius"=>10,"Border"=>TRUE));
 		/* Write the legend box */ 
 		$myPicture->setFontProperties(array("FontName"=>pClass."fonts/Silkscreen.ttf","FontSize"=>6,"R"=>0,"G"=>0,"B"=>0));
 		$PieChart->drawPieLegend(140,160,array("Style"=>LEGEND_NOBORDER,"Mode"=>LEGEND_HORIZONTAL));
 		/* Render the picture (choose the best way) */
 		$myPicture->stroke();
 		//$myPicture->autoOutput("pictures/example.draw3DPie.transparent.png");
 	}
 	function display(){
 		?>
 		Gender Discription:<br />
 		<img src="http://localhost/images/user_gender"></img>
 		<?
 	}
}
?>