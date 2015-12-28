<?php
include('common.php');
class Player extends Common {
	public $fid;
	function __construct(){
		error_reporting(0);
		header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
		header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); // Date in the past
		parent::__construct();
		$this->load->helper('sql_player','sql_game','graph');
		$this->load->library(array('facebook'));
		if(isset($_GET['id']) && !empty($_GET['id']))
     		$this->fid=$_GET['id'];
     	else
     		$this->fid=$this->session->userdata('fid');
     	$config=array(
  			'appId'  	=> 	$this->config->item('fb_appid'),
  			'secret' 	=> 	$this->config->item('fb_appsecret'),
  			'cookie' 	=> 	$this->config->item('fb_cookie'),
     		'domain' 	=> 	$this->config->item('fb_domain'),
		);
     	$this->facebook->init($config);
	}
	public function index(){
     	$this->load->view('player_profile',array(
     						'profile'=>prepareProfile($this->db,$this->fid,$this->facebook),
     						'overall'=>getOverall($this->db, $this->fid)
     						));
	}
	public function ajax_extra_frequent_suits(){
     	$this->load->view('/player/extra_frequent_sets',array('sets'=>getMostFrequentSet($this->db,$this->fid,5)));
	}
	public function ajax_extra_graph_1(){
     	$this->load->view('/player/extra_frequent_sets',array('sets'=>getMostFrequentSet($this->db,$this->fid,5)));
	}
	public function ajax_recent(){
		$this->load->view('/player/recent',array('fid'=>$this->fid,'games'=>getGamesByPlayer($this->db,$this->fid)));
	}
	public function ajax_ranking(){
		$vdata=    getTopPlayers($this->db,5);
		$this->load->view('/player/ranking',array('players'=>$vdata));
	}
	public function ajax_overall(){
		$r=  getMyRatingWithName($this->db,$this->fid);
		$mRanking= getMyRanking($this->db, $r['rating']);
		$this->load->view('/player/over_all',array('ranking'=>$mRanking,
													'rating'=>$r['rating'],
													"game"=>getOverall($this->db, $this->fid),
													"speed"=>getMySpeed($this->db, $this->fid)));
	}
	public function ajax_friends(){
		$friends=filterFriends($this->db,getFriends($this->db,$this->fid,$this->facebook));
		$this->load->view('/player/friends',array('friends'=>$friends));
	}
	public function ajax_played(){
		$this->load->view('/player/played',array('friends'=>getPlayedWith($this->db,$this->fid,5)));
	}
	public function ajax_speed(){
		$this->load->view('/player/speed',array('speeds'=>getSpeed($this->db,5)));
	}
}
?>