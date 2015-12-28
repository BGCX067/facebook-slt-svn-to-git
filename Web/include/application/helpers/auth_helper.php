<?php
	function genKey($fid,$token){
		return sha1($fid.substr($token,sizeof($token)-20,sizeof($token)-10)."ece464");
	}
	function postlogin($fb,$db,$token){
		$fid=$fb->getUser();
		$sql="SELECT `fid` from players WHERE `fid`=? LIMIT 1";
		$query = $db->query($sql,array($fid));
		if ($query->num_rows() > 0){//User exists, update oauth. else insert.
			$sql="UPDATE `players` SET `accesstoken`=? WHERE `fid`= ?";
			$query = $db->query($sql,array($token,$fid));
		}else{
			$sql="INSERT INTO `players` ".
					"(`fid`,`accesstoken`,`dateofbirth`,`gender`,`name`,`relationshipstatus`) VALUES ('$fid','".$token."',?,?,?,?)";
			$me=$fb->api('/me');
			$date=$me['birthday'];
			$data['dateofbirth']=substr($date,6).substr($date,0,2).substr($date,3,2);
			$data['gender']= ($me['gender']=='male')?1:0;
			$data['name']=$me['name'];
			$data['relationshipstatus']=$me['relationship_status'];
			$query = $db->query($sql,$data);
		}
		
	}
?>