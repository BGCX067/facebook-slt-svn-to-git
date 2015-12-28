<?php
include('common.php');
class Game extends Common {
	public $gid;
	function __construct(){
		parent::__construct();
		if(!isset($_GET['gid']) OR empty($_GET['gid']))
			exit("Invalid");
		else
			$this->gid=$_GET['gid'];
		$this->load->helper('sql_game','sql_player');
		$this->load->library(array('facebook'));
	}
	public function index(){
		$this->load->view('game');
	}
}
?>