<h4>Most Frequent Suits</h4>
<p>Most frequent suits tells you what suit have you found the most number of times.</p>
<ol>
<?
foreach($sets as $set){
	$card1=$set['cards']['card1'];
	$card2=$set['cards']['card2'];
	$card3=$set['cards']['card3'];
?>
<li>
<a href="javascript:load_card('/card?id=<?=$card1?>')"><img src="/resources/images/<?=$card1?>.gif" height="74px" width="94px"/></a>
<a href="javascript:load_card('/card?id=<?=$card2?>')"><img src="/resources/images/<?=$card2?>.gif" height="74px" width="94px"/></a>
<a href="javascript:load_card('/card?id=<?=$card3?>')"><img src="/resources/images/<?=$card3?>.gif" height="74px" width="94px"/></a>
<a href="javascript:load_small('/suit?id=<?=$set['cards']['setid']?>')">Found <?=$set['count']?> Times</a>
<?
}
?>
</ol>