<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Auth extends CI_Controller {
	private $ftoken;
	function __construct(){
		parent::__construct();
		$this->load->helper(array('string','auth','cookie'));
		$this->load->library(array('security','facebook'));
	}
	public function index(){
		$app_id = $this->config->item('fb_appid');
		$canvas_page="http://sleepbot.kicks-ass.net:8081/";
     	$auth_url = "http://www.facebook.com/dialog/oauth?client_id=" 
        	    . $app_id . "&redirect_uri=" . urlencode($canvas_page)."&scope=offline_access,read_friendlists,user_birthday";
     	$signed_request = $_REQUEST["signed_request"];
     	list($encoded_sig, $payload) = explode('.', $signed_request, 2); 
     	$data = json_decode(base64_decode(strtr($payload, '-_', '+/')), true);
     	if (empty($data["user_id"])) {
            echo("<script> top.location.href='" . $auth_url . "'</script>");
     	} else {
     		$config=array(
  				'appId'  	=> 	$this->config->item('fb_appid'),
  				'secret' 	=> 	$this->config->item('fb_appsecret'),
  				'cookie' 	=> 	$this->config->item('fb_cookie'),
     			'domain' 	=> 	$this->config->item('fb_domain'),
			);
     		$facebook=$this->facebook;
     		$facebook->init($config);
     		$atoken=$facebook->getAccessToken();
     		$gid=postlogin($facebook,$this->db,$atoken);
     		$this->session->set_userdata(array(
     			"fid"=>$facebook->getUser(),
     			"key"=>genKey($facebook->getUser(),$atoken)
     		));
     		header('Location: /game_list');
     	}
	}
}