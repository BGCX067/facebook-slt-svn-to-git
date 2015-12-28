<?php 
include('common.php');
class Card extends Common {
	public $id;
	function __construct(){
		//error_reporting(0);
		parent::__construct();
		$this->load->helper(array('sql_player','sql_game','sql_suit'));
		if(isset($_GET['id']))
     		$this->id=$_GET['id'];
     	else 
     		exit('Please Specify a valid card');
	}
	function index(){
		$sql="SELECT * from sets WHERE card1=? OR card2=? OR card3=?";
		$query=$this->db->query($sql,array($this->id,$this->id,$this->id));
		if($query->num_rows()==0)
			exit('Invalid Card Selected.');
		else
			$this->load->view('card',array("sets"=>$query->result_array(),"id"=>$this->id));	
	}
}
?>