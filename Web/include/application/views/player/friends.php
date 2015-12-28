<h4>Top Ranked Friends</h4>
<?
foreach($friends as $friend){
?>
<p><img src="https://graph.facebook.com/<?=$friend['fid']?>/picture"/><a href="javascript:load_small('/player?id=<?=$friend['fid']?>')"><?=$friend['name']?></a></p>
<?
}
?>