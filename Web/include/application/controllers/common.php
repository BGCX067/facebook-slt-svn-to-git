<?php
/**
 * This class and its children will verify if the user is logged in already. 
 */
class Common extends CI_Controller {
	function __construct(){
		error_reporting(0);
		parent::__construct();
		$this->load->helper(array('string','auth','cookie'));
		$this->load->library(array('security','facebook'));
		$fid=$this->session->userdata('fid');
		$key=$this->session->userdata('key');
		if(empty($fid) || empty($key)){			
			$app_id = $this->config->item('fb_appid');
			$canvas_page="http://sleepbot.kicks-ass.net:8081/";
     		$auth_url = "http://www.facebook.com/dialog/oauth?client_id=" 
        	    . $app_id . "&redirect_uri=" . urlencode($canvas_page)."&scope=read_friendlists,offline_access,user_religion_politics,user_birthday";
        	    exit("<script> top.location.href='" . $auth_url . "'</script>");    
		}
	}
}
?>