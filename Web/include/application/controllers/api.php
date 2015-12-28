<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Api extends CI_Controller {
	
	function __construct(){
		parent::__construct();
		$this->load->helper(array('string','auth'));
		$this->load->library(array('security','facebook'));
		//error_reporting(0);
	}
	function auth(){
		$fid=$_POST['fid'];
		$key=$_POST['key'];
		$sql="SELECT `accesstoken`,`name` from players where `fid`=? LIMIT 1";
		$query = $this->db->query($sql,array($fid));
		if ($query->num_rows() > 0){
			$row = $query->row();
			$akey=genKey($fid,$row->accesstoken);
			echo (strcmp($akey,$key)==0) ? "OK,".$row->name :"";
		}
	}
}