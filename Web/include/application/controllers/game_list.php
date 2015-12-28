<?php
include('common.php');
class Game_list extends Common {
	function __construct(){
		error_reporting(0);
		parent::__construct();
		$this->load->helper('sql_game','sql_player');
		$this->load->library(array('facebook'));
	}
	public function index(){
     	$this->load->view('game_list',array("appId"=>$this->config->item('fb_appid')));
	}	
	public function load(){
		$mode;
		if(!isset($_GET['mode']) || empty($_GET['mode']))
			$mode='joinable';
		else 
			$mode=$_GET['mode'];
		//No cache for Game List
		header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
		header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); // Date in the past
		$max_game=$this->config->item('max_game');
		if($mode=='friends'){
     		$config=array(
  				'appId'  	=> 	$this->config->item('fb_appid'),
  				'secret' 	=> 	$this->config->item('fb_appsecret'),
  				'cookie' 	=> 	$this->config->item('fb_cookie'),
     			'domain' 	=> 	$this->config->item('fb_domain'),
			);
     		$this->facebook->init($config);
			$query=getGames($this->db,$max_game,$this->config->item('max_player'),$mode,$this->session->userdata('fid'),$this->facebook);
		}else
			$query=getGames($this->db,$max_game,$this->config->item('max_player'),$mode);
		$games=array();
		foreach ($query->result_array() as $row){
    		$game=array();
    		$game['id']=$row['gid'];
    		$game['state']=$row['state'];
    		$game['players']=getPlayersFromGid($this->db,$row['gid']);
    		$games[]=$game;
		}
		$rows=array();
		if($query->num_rows()<$max_game && $mode=='joinable'){
			 $rows=createGames($this->db,$max_game-$query->num_rows());
			foreach($rows as $row){
    			$game=array();
    			$game['id']=$row['gid'];
    			$game['state']=0;
    			$game['players']=array();
    			$games[]=$game;
			}
		}
     	$this->load->view('game_list_ajax',array(
     			"games"=>$games,
        		"appId"=>$this->config->item('fb_appid'),
     			"mode"=>$mode));
	}
	public function start(){
		//Should i check key fid here? prob no..redundent.
        $vdata=array("fid" => $this->session->userdata('fid'),
        	   		 "gid" => $this->input->get('gid'),
        	   		 "key" => $this->session->userdata('key'),
        			 "appId"=>$this->config->item('fb_appid')
        );
     	$this->load->view('applet',$vdata);
	}
}
?>