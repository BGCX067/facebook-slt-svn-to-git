<h4>Quickest Finders</h4>
<p>(lowest average/suit)</p>
<?php
foreach($speeds as $player){	
?>
<p><a href="javascript:load_small('/player?id=<?=$player['fid']?>')"><?=$player['name']?></a> <?=substr($player['average']/1000,0,5)?>s</p>
<?php }?>