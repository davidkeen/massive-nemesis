/*
Script: DHTML Easy Countdown (Development Version - not intended for production use due to uncompressed size)
Description: Counts down or up from a date (this is the non-XHTML version)
Author: Andrew Urquhart
Home: http://andrewu.co.uk/clj/countdown/
History:
20040117 0035UTC	v1		Andrew Urquhart		Created
20040119 1943UTC	v1.1	"					Made more accessible/easier to use
20040317 1548UTC	v1.2	"					Implemented a less intrusive error message
20040331 1408BST	v1.3	"					Attempts to append to the current onload schedule, rather than overriding it
20050210 1558GMT	v1.4	"					Saves a short-cut to each countdown Id element and re-uses that rather than continually calling getElementById - faster. Also reduced all nouns to short-versions to decrease size of script
20050216 0018GMT	v1.41	"					Added support for IE4
20051231 0210GMT	v1.5	"					Switched to custom date format and custom date parser for better internationalisation and cross-browser compatibility (should fix reports of recent bugs in Mac browsers)
20060101 1649GMT	v1.52	"					Made counter update 0.1s after the last whole second to ensure that the browser doesn't render the display at the exact moment the counter changes - otherwise can appear to miss a second out
20070414 2313BST	v1.53	"					Added 'typeof CD_T' check to CD_T() and test for CD_OBJS[id] in CD_D incase references don't exist when the timeout resolves
*/

// Tick (countdownId, eventDate)
function CD_T(id, e) {
	var n = new Date();
	CD_D(+n, id, e);
	setTimeout("if(typeof CD_T=='function'){CD_T('" + id + "'," + e + ")}", 104); // We offset from 1100 so that our clock ticks every second (the millisecond correction each loop sees to that), but updates 0.1s after every whole second so that we don't accidentally read the same Date() twice in the same second
};

// Calculate time and update display (dateNow, countdownId, eventDate)
function CD_D(n, id, e) {
	var ms = e - n;
	if (ms <= 0) ms *= -1;
	var d = Math.floor(ms/864E5);
	ms -= d*864E5;
	var h = Math.floor(ms/36E5);
	ms -= h*36E5;
	var m = Math.floor(ms/6E4);
	ms -= m*6E4;
	var s = Math.floor(ms/1E3);
	ms -= s*1E3;
	var t = Math.floor(ms/1E2);

	// If you want to manually customise the counter display, then edit this line:
	if (CD_OBJS[id]) {
		CD_OBJS[id].innerHTML = "<img src='snake.gif' /> " + CD_ZP(d) + "d " + CD_ZP(h) + "h " + CD_ZP(m) + "m " + CD_ZP(s) + "." + t + "s ";
	}
};

// Prefix single integers with a zero
function CD_ZP(i) {
	return (i<10 ? "0" + i : i);
};

function CD_ZP3(i) {
	if (i < 10) {
		return "00" + i;
	} else if (i < 100) {
		return "0" + i;
	} else {
		return i;
	}
}

// Initialisation
function CD_Init() {
	var pref = "countdown";
	var objH = 1; // temp boolean true value
	if (document.getElementById || document.all) {
		for (var i=1; objH; ++i) {
			var id	= pref + i;
			objH	= document.getElementById ? document.getElementById(id) : document.all[id];

			if (objH && (typeof objH.innerHTML) != 'undefined') {
				var s	= objH.innerHTML;
				var dt	= CD_Parse(s);
				if (!isNaN(dt)) {
					CD_OBJS[id] = objH; // Store global reference to countdown element object
					CD_T(id, dt.valueOf());
					if (objH.style) {
						objH.style.visibility = "visible";
					}
				}
				else {
					objH.innerHTML = s + "<a href=\"http://andrewu.co.uk/clj/countdown/\" title=\"Countdown Error: Invalid date format used, check documentation (see link)\">*</a>";
				}
			}
		}
	}
};

// Get Date() object from 2006-01-01 00:00:00 GMT+00:00 date format
function CD_Parse(strDate) {
	// Pattern match to a countdown date
	var objReDte = /(\d{4})\-(\d{1,2})\-(\d{1,2})\s+(\d{1,2}):(\d{1,2}):(\d{0,2})\s+GMT([+\-])(\d{1,2}):?(\d{1,2})?/;

	if (strDate.match(objReDte)) {
		// Start with a default date and build it up into the countdown date through Date setter methods
		var d = new Date(0);

		d.setUTCFullYear(+RegExp.$1,+RegExp.$2-1,+RegExp.$3); // Set YYYY-MM-DD directly as UTC
		d.setUTCHours(+RegExp.$4,+RegExp.$5,+RegExp.$6); // Set HH:MM:SS directly as UTC

		// If there is a timezone offset specified then we need to compensate for the offset from UTC
		var tzs	= (RegExp.$7 == "-" ? -1 : 1); // Timezone sign
		var tzh = +RegExp.$8; // Get requested timezone offset HH (offset ahead of UTC - may be negative)
		var tzm = +RegExp.$9; // Get requested timezone offset MM (offset ahead of UTC - always positive)
		if (tzh) {
			d.setUTCHours(d.getUTCHours() - tzh*tzs); // Compensate for timezone HH offset from UTC
		}
		if (tzm) {
			d.setUTCMinutes(d.getUTCMinutes() - tzm*tzs); // Compensate for timezone MM offset, depending on whether the requested MM offset is ahead or behind of UTC
		}
		return d; // Date now correctly parsed into a Date object correctly offset from UTC internally regardless of users current timezone.
	}
	else {
		return NaN; // Didn't match required date format
	};
};

var CD_OBJS = new Object();

// Try not to commandeer the default onload handler if possible
if (window.attachEvent) {
	window.attachEvent('onload', CD_Init);
}
else if (window.addEventListener) {
	window.addEventListener("load", CD_Init, false);
}
else {
	window.onload = CD_Init;
}
