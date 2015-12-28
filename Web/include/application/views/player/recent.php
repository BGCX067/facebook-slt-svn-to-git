<h4>Recent Activities</h4>
<ul>
<?
foreach($games as $game){
?>
<li>Just <?=$game['result']?> <a href="javascript:load_small('/game?gid=<?=$game['gid']?>')">Game <?=$game['gid']?></a> against
	<? 	$players=$game['players'];
		$length=sizeof($game['players'])-1;
		for($i=0;$i<=$length;$i++){
			$player=$players[$i];
			if(is_array($player) && isset($player['fid']) && $player['fid']!=$fid){?>
				<a href="javascript:load_small('/player?id=<?=$player['fid']?>')"><?=$player['name']?></a><? if ($i!=$length) {?>, <?}
		}
		}
	?> 
</li>
<?
}
?>
</ul>