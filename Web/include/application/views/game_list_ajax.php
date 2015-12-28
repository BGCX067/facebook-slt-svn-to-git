<?
if(sizeof($games)>0){
?>
<table>
<tbody>
<tr class='alt-row'>
<td>Game ID</td>
<td>Players</td>
<td></td>
</tr>
<?
$i=0;
foreach($games as $game){
?>
<tr<? if ($i%2==1) echo " class='alt-row'"?>>
<td><?=$game['id']?></td>
<td><? 	foreach($game['players'] as $player){
		if(is_array($player) && isset($player['fid'])){?>
			<a href="javascript:load_small('/player?id=<?=$player['fid']?>')"><?=$player['name']?></a>, <?
		}
		}
		$i++;
		?>
</td>
<?
if($game['state']==0){
	$text='Join';
	$link='/game_list/start?gid='.$game['id'];
}else{
	$text='View';
	$link='/game?gid='.$game['id'];
}
?>
<td><a href="javascript:load('<?=$link?>')"><?=$text?></a></td>
</tr>
<?
}
?>
</tbody>
</table>
<?php
}else 
  echo "<p align='center'>There are no games avaliable.</p>"; 
?>