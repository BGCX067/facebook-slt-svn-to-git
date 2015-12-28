<?php
include 'common.php';
class Friends extends Common {
	public $facebook;
	public $atoken;
	function __construct(){
		parent::__construct();
		$this->load->helper('sql_game');
	}
	public function index(){
		echo "404";
	}
	public function gameList(){
		$config=array(
  				'appId'  	=> 	$this->config->item('fb_appid'),
  				'secret' 	=> 	$this->config->item('fb_appsecret'),
  				'cookie' 	=> 	$this->config->item('fb_cookie'),
			);
     	$this->facebook->init($config);
     	$this->atoken=$this->facebook->getAccessToken();
		$fid=$this->facebook->getUser();
     	$friendList=$this->facebook->api('/me/friendlists?access_token='.base64_decode(strtr($this->atoken, '-_', '+/')));
     	$lists=$friendList['data'];
     	print_r($lists);
	}
}
?>