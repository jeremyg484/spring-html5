importScripts("../jquery/jquery.hive.pollen.js");

var basePath = location.host;
if (location.pathname.search(/spring-html5/) > -1) { 
    basePath += "/spring-html5";
}
var last = null;

function start(offline) {
	//Local simulation for browsers without WebSocket
	var connectionFailed = false;
	var messagesReceived = false;
	if (!self.WebSocket || offline) {
		postMessage("Reverting to cached data.js");
		importScripts("data.js");
		data.reverse();
		for ( var i = 0; i < data.length; i++) {
			var c = data.shift();
			if (!last || last.moduleName != c.moduleName || last.timeStamp != c.timeStamp) {
				last = c;
				c.order = i;
				postMessage(c);
			}
		}
	} else {
		var messageChannelUrl = "ws://" + basePath + "/pubsub/commitRecords";  
		var socket = new WebSocket(messageChannelUrl);
		var count = 0;
		postMessage("Opening the WebSocket at "+messageChannelUrl);
		socket.onopen = function(event) {
			//TODO - Move this out to be triggered with a button
			var launchJobUrl = "http://" + basePath + "/commitRecordProcessor";
			postMessage("Sending GET with ?command=start to "+launchJobUrl);
			$.ajax.get({url : launchJobUrl, data : {command : "start"}});
		}
		socket.onmessage = function(event) {
			messagesReceived = true;
			var data = JSON.parse(event.data);
			var c = null;
			while (c = data.shift()) {
				//if (!last || last.moduleName != c.moduleName || last.timeStamp != c.timeStamp) {
				//	last = c;
				//c.order = ++count;
				postMessage(c);
			}
		}
		socket.onerror = function(event) {
			connectionFailed = true;
		}
		socket.onclose = function(event) {
			postMessage("Connection to "+messageChannelUrl+" was closed.");
			start(true);
		}
	}
}

function stop() {

}

addEventListener("message", function(event) {
	var data = event.data;
	switch (data) {
	case 'start':
		start(false);
		break;
	case 'stop':
		stop();
		break;
	}
}, false);