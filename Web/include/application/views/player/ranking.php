<h4>Top Rated Players</h4>
<?php
foreach($players as $player){
?><p><a href="javascript:load_small('/player?id=<?=$player['fid']?>')"><?=$player['name']?></a> <?=$player['rating']?></p><?
}
?>