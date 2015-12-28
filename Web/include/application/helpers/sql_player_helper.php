<?php
require_once APPPATH.'/helpers/sql_game_helper.php';
require_once APPPATH.'/helpers/graph_helper.php';

function prepareProfile($db,$fid){
	$sql="SELECT `fid`,`name` FROM players WHERE fid= ? LIMIT 1";
	$query=$db->query($sql,array($fid));
    $result=$query->row_array();
	return $result;
}
function getOverall($db,$fid){
	$sql="SELECT outcome,COUNT(*) as c FROM plays WHERE playerid=? GROUP BY outcome ORDER BY outcome ASC";
	$query=$db->query($sql,array($fid));
    return $query->result_array();
}
function getMyRatingWithName($db,$fid){
	$sql="SELECT `name`,`rating` FROM players WHERE fid= ? LIMIT 1";
	$query=$db->query($sql,array($fid));
    return $query->row_array();
}
function getFriends($db,$userid,$fb){
	$sql="SELECT `friends` FROM players WHERE fid= ? LIMIT 1";
	$query=$db->query($sql,array($userid));
    $row=$query->row_array();
    return empty($row['friends'])? updateFriends($db,$fb,$userid)
    					: json_decode($row['friends'],TRUE);
}
function getSpeed($db,$limit){
	$sql="SELECT finder as fid,name,COUNT(*) as suittotal,SUM(timetaken) as timetotal,AVG(timetaken) as average,MIN(timetaken) as min,MAX(timetaken) as max from found,players WHERE finder=fid GROUP BY finder ORDER BY average ASC LIMIT ?";
	$query=$db->query($sql,array($limit));
    return $query->result_array();
}
function getMySpeed($db,$fid){
	$sql="SELECT COUNT(*) as suittotal,SUM(timetaken) as timetotal,AVG(timetaken) as average,MIN(timetaken) as min,MAX(timetaken) as max FROM found WHERE finder=? LIMIT 1";
	$query=$db->query($sql,array($fid));
    return $query->row_array();
}
function getMostFrequentSet($db,$fid,$limit){
	$result=array();
	$sql="SELECT setfound, COUNT( * ) AS c FROM found WHERE finder =? GROUP BY setfound ORDER BY c LIMIT ?";
	$query=$db->query($sql,array($fid,$limit));
	$ids=array();
	$i=0;
	if($query->num_rows()<=0)
		return array();
	foreach ($query->result_array() as $row){
		$result['s'.$row['setfound']]=array('count'=>$row['c']);
		$ids[]=$row['setfound'];
		$i++;
	}
	//Nested Query? I don't think I need the OR here.
	$k=$i-1;
	$sql="SELECT setid,card1,card2,card3 FROM sets WHERE ";
	for($j=0;$j<$k;$j++){
		$sql.="setid=? OR ";
	}
	$sql.="setid=?";
	$query=$db->query($sql,$ids);
	foreach ($query->result_array() as $row)
		$result['s'.$row['setid']]['cards']=$row;
	return $result;
}
function getTopPlayers($db,$num=10){
	$sql="SELECT `name`,`fid`,`rating` FROM players ORDER BY `rating` DESC LIMIT ?";
	$query=$db->query($sql,array($num));
	return $query->result_array();
}
function getMyRanking($db,$rating){
	$sql="SELECT COUNT(*) as r FROM players WHERE `rating`>?";
	$query=$db->query($sql,array($rating));
    $r=$query->row_array();
    return $r['r']+1;
}
function getGamesByPlayer($db,$fid){
	$sql="SELECT DISTINCT `gameid`,`outcome` FROM plays WHERE `playerid`=? AND `outcome`>? ORDER BY gameid DESC LIMIT 5";
	$result=array('lost','won','tied');
	$query=$db->query($sql,array($fid,-1));
	$games=array();
	foreach ($query->result_array() as $row){
    	$game=array();
    	$game['gid']=$row['gameid'];
    	$game['result']=$result[$row['outcome']];
    	$game['players']=getPlayersFromGid($db,$row['gameid']);
    	$games[]=$game;
	}
	return $games;
}
function updateFriends($db,$fb,$fid){
	$api=$fb->api("/$fid/friends");
	$friends=$api['data'];
	$friendIds=array();
	foreach($friends as $friend){
		$friendIds[]=$friend['id'];
	}
	$sql="UPDATE players SET friends=? WHERE fid= ?";
	$query=$db->query($sql,array(json_encode($friendIds),$fid));
	return $friendIds;
}
function filterFriends($db,$friends){
	//This is not necessary. Can be used as nested query.
	$sql="SELECT `name`,`fid` FROM players WHERE ";
	$length=sizeof($friends)-1;
	for($j=0;$j<$length;$j++){
		$sql.="fid=? OR ";
	}
	$sql.="fid=? ORDER BY rating DESC LIMIT 5";
	$query=$db->query($sql,$friends);
	return $query->result_array();
}
function getPlayedWith($db,$fid,$limit){
	$sql="SELECT `name`,`fid`,COUNT(gameid) as count FROM players,plays 
		  WHERE fid=playerid AND 
		  		gameid IN(
					SELECT `gameid` FROM `plays` WHERE `playerid`=?
		  		)
		  GROUP BY `fid` HAVING `fid`!=? ORDER BY count DESC LIMIT ?";
	$query=$db->query($sql,array($fid,$fid,$limit));
	return $query->result_array();
}
?>