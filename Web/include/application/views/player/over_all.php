<?php
$total=0;
$lost=0;
$won=0;
$tie=0;
$dc=0;
foreach($game as $outcome){
	if($outcome['outcome']==-1){
		$dc+=$outcome['c'];
	}
	if($outcome['outcome']==0){
		$lost+=$outcome['c'];
	}
	if($outcome['outcome']==1){
		$won+=$outcome['c'];
	}
	if($outcome['outcome']==2){
		$tie+=$outcome['c'];
	}
}
$total=$dc+$lost+$won+$tie;
?>
<h4>Overall Stats</h4>
<p>Current Rating: <?=$rating?> (Network Rank: <?=$ranking?>)</p>
<p>Games Played: <?=$total?> (Won:<?=$won?> Lost:<?=$lost?> Tied:<?=$tie?> Dc-ed:<?=$dc?>/<?=substr((($dc/$total)*100),0,5)?>%)</p>
<p>Winning Percentage: <?=substr((($won/$total)*100),0,4)?>% (<?=substr(($won/($won+$tie+$lost)*100),0,4)?>% without dc-ed games)</p>
<p>Time spent finding a suit: </p>
<ul>
<li>Average Time to find a suit: <?=substr($speed['average']/1000,0,6)?> seconds</li>
<li>Fastest: <?=substr($speed['min']/1000,0,6)?> seconds</li>
<li>Slowest: <?=substr($speed['max']/1000,0,6)?> seconds</li>
<li>Total Suits Found: <?=substr($speed['suittotal'],0,6)?> Suits</li>
<li>Total Time Spent: <?=substr($speed['timetotal']/60000,0,6)?> minutes</li>
<li>Average Suits Found per Game: <?=substr($speed['suittotal']/$total,0,6)?> Suits</li>
</ul>
