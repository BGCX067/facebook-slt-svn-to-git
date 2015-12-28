<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Suit Profile</title>
<link rel="stylesheet" type="text/css" href="/resources/card.css"/>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

<script language="Javascript" type="text/javascript">
function load_card(address) {
	window.open(address,'','scrollbars=yes,menubar=no,height=600,width=540,resizable=no,toolbar=no,location=no,status=no');
}
function load_small(address) {
	window.open(address,'','scrollbars=yes,menubar=no,height=1000,width=1140,resizable=yes,toolbar=no,location=no,status=no');
}
</script>
</head>
<body>
<div class="container">
  <header>
    <wtext align="center">Card Profile</wtext>
  </header>
  <article class="content">
  <section>
    <p><div align="center"><img src="/resources/images/<?=$id?>.gif"/></div></p>
  </section>
    <section>
    <h4>Suits with this card</h4>
    <ol>
    <? 
    foreach($sets as $set){
    ?><li>
	<a href="javascript:load_card('/card?id=<?=$set['card1']?>')"><img src="/resources/images/<?=$set['card1']?>.gif" height="74px" width="94px"/></a>
	<a href="javascript:load_card('/card?id=<?=$set['card2']?>')"><img src="/resources/images/<?=$set['card2']?>.gif" height="74px" width="94px"/></a>
	<a href="javascript:load_card('/card?id=<?=$set['card3']?>')"><img src="/resources/images/<?=$set['card3']?>.gif" height="74px" width="94px"/></a>
	<a href="javascript:load_small('/suit?id=<?=$set['setid']?>')">View Suit Info</a>
	</li>
    <?
    }
    ?>
    </ol>
    </section>
  </article>
  <footer>
    <wtext align="center">Copyright 2011 Alina, Chris, and Edison</wtext>
  </footer>
</div>
</body>
</html>
