<!DOCTYPE html>
<html lang="en">
<head>
<title>Suit for Facebook - Behavioral Research Project for Cooper Union Class ECE464</title>
</head>
<body>
	<object type="application/x-java-applet" height="500" width="800" >
		<param name="code" value="set.gui.ClientApplet" > 
		<param name="archive" value="/resources/Suit.jar" >
		<param name="fid" value=<?=$fid?> > 
		<param name="gid" value=<?=$gid?> >
		<param name="key" value=<?=$key?> >
			Applet failed to run.  No Java plug-in was found.
	</object>
	<div id="fb-root"></div><script src="http://connect.facebook.net/en_US/all.js#appId=<?=$appId?>&amp;xfbml=1"></script><fb:live-stream event_app_id="<?=$appId?>" width="600" height="500" xid="<?=$gid?>" always_post_to_friends="false"></fb:live-stream> 
</body>
</html>