<?php
require_once 'sql_player_helper.php';
//I did not use a model because it is not necessary and not wanted for the database class. 
	function getGames($db,$max_game,$max_player,$mode,$fid=false,$facebook=false){
		$modes=array('joinable'=>0,'started'=>1,'ended'=>2,'all'=>-1,'friends'=>-2,'friends_all'=>-3);
		$m=$modes[$mode];
		if($m==-2){
			$friends=filterFriends($db,getFriends($db,$fid,$facebook));
			$sql="SELECT games.gid,games.state,games.numplayers ".
				 "FROM games,plays ".
				 "WHERE plays.gameid=games.gid AND games.state=0 AND games.numplayers<=?";
			$s=sizeof($friends);
			if(is_array($friends)){
				if($s==0)
					return array();
				if($s==1)
					$sql_mid=" AND plays.playerid=?";
				else{
					$s--;
					$sql_mid=" AND (";
					for($i=0;$i<$s;$i++)
						$sql_mid.="plays.playerid=? OR ";
					$sql_mid.='plays.playerid=?)';
					$sql.=$sql_mid;
				}
			}else
				return array();
			$sql.="ORDER BY gid DESC LIMIT ?";
			$ids=array($max_player);
			foreach($friends as $friend)
				$ids[]=$friend['fid'];
			$ids[]=$max_game;
			$query=$db->query($sql,$ids);
			return $query;
		}else{
			if($m!=-1){
				$sql="SELECT `gid`,`state`,`numplayers` FROM `games` WHERE `state`=? AND `numplayers`<= ? ORDER BY gid DESC LIMIT ?";
				$query=$db->query($sql,array($m,$max_game,$max_player));
			}else{
				$sql="SELECT `gid`,`state`,`numplayers` FROM `games` ORDER BY gid DESC LIMIT ?";
				$query=$db->query($sql,array($max_game));
			}
		}
		return $query;
	}
	
	function createGames($db,$num=1){
		$results=array();
		for($i=0;$i<$num;$i++){
			$sql="INSERT INTO `games` VALUES()";
			$db->query($sql);
			$results[$i]=$db->insert_id();
		}
		return $results;
	}
	
	function getPlayersFromGid($db,$gid){
		$sql="SELECT players.fid,players.name FROM plays,players WHERE plays.gameid=? AND players.fid=plays.playerid";
		$r=array();
		foreach($db->query($sql,array($gid))->result_array() as $row){
			$row['name']=substr($row['name'],0,strpos($row['name'],' '));
			$r[]=$row;
		}
		return $r;
	}
?>