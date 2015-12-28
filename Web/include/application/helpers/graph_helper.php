<?php
function getGenderCounts($db){
	$sql="SELECT `gender` , (COUNT(*)/(SELECT COUNT(*) FROM players))*100 as c FROM players GROUP BY  `gender` ";
	$query=$db->query($sql);
    $gender=array();
	foreach ($query->result_array() as $key=>$row){
		$gender[$key]=$row['c'];
	}
	return $gender;	
}

// returnds labels as a string (that Highchart can use)
function getTimeLabels() {
	return "'0-1', '1-2', '2-3', '3-4', '4-5', '5-6', '6-7', '7-8', '8-9', '9-10', '> 10'";
}

// gender: 0,female; 1,male
function getGenderSeries($gender_arr, $gender, $numbkts) {
	for($i=0; $i < $numbkts-1 ; $i++) {
		$result.= $gender_arr[$gender][$i].", ";
	}
	$result.= $gender_arr[$gender][$numbkts-1];
	return $result;
}

function getBothGenderData($gender_arr, $numbkts) {
	for($i=0; $i < $numbkts-1; $i++) {
		$result.= ($gender_arr[0][$i] + $gender_arr[1][$i]).", ";
	}
	$result.= $gender_arr[0][$numbkts-1] + $gender_arr[1][$numbkts-1];
	return $result;
}

// time is in bucket sizes of 1 second
function getTimeHistData($db, $numbkts) {
	$sql = "SELECT gender, ";
	for($i=0; $i < $numbkts-1; $i++) {
		$sql.= "sum(CASE WHEN time >= ".$i." AND time <= ".($i+1)." THEN 1 ELSE 0 END) as '".$i."', ";
	}
	$sql.= "sum(CASE WHEN time >= ".($numbkts-1)." THEN 1 ELSE 0 END) as '".($numbkts-1)."' ";
	$sql.= "FROM (SELECT finder, gender, AVG(timetaken)/1000 AS time FROM found, players 
					WHERE finder = fid GROUP BY finder, gender) as timeavg
			GROUP BY gender ORDER BY gender ASC";
	$query = $db->query($sql);
    $result = $query->result_array();
	return $result;
}

function getRatingBucketSize($db, $numbkts) {
	$sql = "SELECT r2.rating, (SUM(r1.lesser)/(SELECT COUNT(*) FROM players)) AS p
			FROM (SELECT COUNT(*) AS lesser, rating FROM players GROUP BY rating) r1
			JOIN (SELECT COUNT(*) AS greater, rating FROM players GROUP BY rating) r2
			ON r1.rating < r2.rating
			GROUP BY r2.rating 
			HAVING p >= 0.9
			ORDER BY p ASC
			LIMIT 1";
	$query = $db->query($sql);
	$result = $query->row_array();
	$bucketsize = ROUND((($result['rating']/($numbkts-1))-1)/10 + 0.5)*10;
	return $bucketsize;
}

function getRatingLabels($bktsize, $numbkts) {
	for($i=1; $i < $numbkts; $i++) {
		$results.= "'".$i*$bktsize."', "; 
	}
	$results.= "'> ".($bktsize*($numbkts-1))."'";
	return $results;
}

function getRatingHistData($db, $numbkts, $bktsize) {
	$sql = "SELECT gender, ";
	for($i=0; $i < $numbkts-1; $i++) {
		$sql.= "sum(CASE WHEN rating >= ".$i*$bktsize." AND 
					rating <= ".($i+1)*$bktsize." THEN 1 ELSE 0 END) as '".$i."', ";
	}
	$sql.= "sum(CASE WHEN rating >= ".($numbkts-1)*$bktsize." THEN 1 ELSE 0 END) as '".($numbkts-1)."' ";
	$sql.="FROM players GROUP BY gender ORDER BY gender ASC";
	$query = $db->query($sql);
	$result = $query->result_array();
	return $result;
}

function getTimeColorData($db) {
	$sql = "SELECT gender, color, AVG(timetaken)/1000 as timeavg FROM found, sets, players 
			WHERE setfound = setid AND color != -1 AND finder = fid 
			GROUP BY gender, color ORDER BY gender, color ASC";
	$query = $db->query($sql);
	return $query->result_array();
}

function getTimeAttribData($db, $attrib) {
	$sql = "SELECT gender, ".$attrib.", AVG(timetaken)/1000 as timeavg FROM found, sets, players 
			WHERE setfound = setid AND ".$attrib." != -1 AND finder = fid 
			GROUP BY gender, ".$attrib." ORDER BY gender, ".$attrib." ASC";
	$query = $db->query($sql);
	return $query->result_array();
}

function getTimeAttribTypeGender($attrib_arr, $gender) {
	if ($gender == 0) {
		$result = round($attrib_arr[0]['timeavg'],2).", ".
					round($attrib_arr[1]['timeavg'],2).", ".
					round($attrib_arr[2]['timeavg'],2);
	}
	else {$result = round($attrib_arr[3]['timeavg'],2).", ".
					round($attrib_arr[4]['timeavg'],2).", ".
					round($attrib_arr[5]['timeavg'],2);
	}
	return $result;
}

function getTimeAttribTypeBothGender($attrib_arr) {
	$result = (round((($attrib_arr[0]['timeavg'] + $attrib_arr[3]['timeavg'])/2),2)).", ".
				(round((($attrib_arr[1]['timeavg'] + $attrib_arr[4]['timeavg'])/2),2)).", ".
				(round((($attrib_arr[2]['timeavg'] + $attrib_arr[5]['timeavg'])/2),2));
	return $result;
}

function getTimeAllAttribGender($color_arr, $number_arr, $fill_arr, $shape_arr, $gender) {
	if ($gender == 0) 
		$j = 0;
	else $j = 3;
	for ($i = 0; $i < 3 ; $i++, $j++) {
		$colortot += $color_arr[$j]['timeavg'];
		$numbertot += $number_arr[$j]['timeavg'];
		$filltot += $fill_arr[$j]['timeavg'];
		$shapetot += $shape_arr[$j]['timeavg'];
	}
	$result = (round(($colortot/3),2)).", ".(round(($numbertot/3),2)).", ".
				(round(($filltot/3),2)).", ".(round(($shapetot/3),2));
	return $result;
}

function getTimeAllAttribBothGender($color_arr, $number_arr, $fill_arr, $shape_arr) {
	for ($i = 0; $i < 6 ; $i++, $j++) {
		$colortot += $color_arr[$i]['timeavg'];
		$numbertot += $number_arr[$i]['timeavg'];
		$filltot += $fill_arr[$i]['timeavg'];
		$shapetot += $shape_arr[$i]['timeavg'];
	}
	$result = (round(($colortot/6),2)).", ".(round(($numbertot/6),2)).", ".
				(round(($filltot/6),2)).", ".(round(($shapetot/6),2));
	return $result;
}

function getColorLabels() {
	return "'green', 'red', 'blue'";
}

function getNumberLabels() {
	return "'one', 'two', 'three'";
}

function getFillLabels() {
	return "'none', 'shaded', 'solid'";
}

function getShapeLabels() {
	return "'square', 'circle', 'triangle'";
}
?>