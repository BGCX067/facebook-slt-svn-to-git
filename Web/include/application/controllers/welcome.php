<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Welcome extends CI_Controller {
	
	function __construct(){
		parent::__construct();
		$this->load->helper('url');
	}
	
	public function index(){
    	redirect("http://apps.facebook.com/suit_match/");
	}
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */