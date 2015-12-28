<?php
class Aggregate extends CI_Controller {
	function __construct(){
		error_reporting(0);
		//header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
		//header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); // Date in the past
		parent::__construct();
		$this->load->helper('graph'); 
	}
	function index(){
 		$gender = getGenderCounts($this->db); 
 		
		$timelabels = getTimeLabels();
		$numtimebkts = 11;
		$gendertime = getTimeHistData($this->db, $numtimebkts);
		$timedata = getBothGenderData($gendertime, $numtimebkts);
		$femtime = getGenderSeries($gendertime, 0, $numtimebkts);
		$maletime = getGenderSeries($gendertime, 1, $numtimebkts);
		
		$numrtgbkts = 11;
		$rtgbktsize = getRatingBucketSize($this->db, $numrtgbkts);
		$ratinglabels = getRatingLabels($rtgbktsize, $numrtgbkts);
		$ratingdata = getRatingHistData($this->db, $numrtgbkts, $rtgbktsize);
		$femrating = getGenderSeries($ratingdata, 0, $numrtgbkts);
		$malerating = getGenderSeries($ratingdata, 1, $numrtgbkts);
		
		$colorlabels = getColorLabels();
		$numberlabels = getNumberLabels();
		$filllabels = getFillLabels();
		$shapelabels = getShapeLabels();
		
		$colordataraw = getTimeAttribData($this->db, 'color');
		$numberdataraw = getTimeAttribData($this->db, 'number');
		$filldataraw = getTimeAttribData($this->db, 'fill');
		$shapedataraw = getTimeAttribData($this->db, 'shape');
		
		$attribdata = getTimeAllAttribBothGender($colordataraw, $numberdataraw, 
													$filldataraw, $shapedataraw);
		$femattrib = getTimeAllAttribGender($colordataraw, $numberdataraw, 
													$filldataraw, $shapedataraw, 0);
		$maleattrib = getTimeAllAttribGender($colordataraw, $numberdataraw, 
													$filldataraw, $shapedataraw, 1);
		/*
		print_r($colordataraw);
		print_r($numberdataraw);
		print_r($filldataraw);
		print_r($shapedataraw);
		print_r($femattrib);
		print_r($maleattrib);
		print_r($attribdata);
		*/
		
		$colordata = getTimeAttribTypeBothGender($colordataraw);
		$femcolor = getTimeAttribTypeGender($colordataraw, 0);
		$malecolor = getTimeAttribTypeGender($colordataraw, 1);
		
		$numberdata = getTimeAttribTypeBothGender($numberdataraw);
		$femnumber = getTimeAttribTypeGender($numberdataraw, 0);
		$malenumber = getTimeAttribTypeGender($numberdataraw, 1);
		
		$filldata = getTimeAttribTypeBothGender($filldataraw);
		$femfill = getTimeAttribTypeGender($filldataraw, 0);
		$malefill = getTimeAttribTypeGender($filldataraw, 1);
		
		$shapedata = getTimeAttribTypeBothGender($shapedataraw);
		$femshape = getTimeAttribTypeGender($shapedataraw, 0);
		$maleshape = getTimeAttribTypeGender($shapedataraw, 1);

		$this->load->view('aggregate_graph', array('gender'=>$gender, 
		
													'timelabels'=>$timelabels,'timedata'=>$timedata,
													'femtime'=>$femtime, 'maletime'=>$maletime,
		
													'ratinglabels'=>$ratinglabels,
													'femrating'=>$femrating, 'malerating'=>$malerating,
		
													'attribdata'=>$attribdata, 'femattrib'=>$femattrib,
													'maleattrib'=>$maleattrib, 
		
													'colorlabels'=>$colorlabels, 'colordata'=>$colordata,
													'femcolor'=>$femcolor, 'malecolor'=>$malecolor,
		
													'numberlabels'=>$numberlabels, 'numberdata'=>$numberdata,
													'femnumber'=>$femnumber, 'malenumber'=>$malenumber,
		
													'filllabels'=>$filllabels, 'filldata'=>$filldata,
													'femfill'=>$femfill, 'malefill'=>$malefill,
		
													'shapelabels'=>$shapelabels, 'shapedata'=>$shapedata,
													'femshape'=>$femshape, 'maleshape'=>$maleshape));
													
	}
}
?>