var socket = new WebSocket("ws://127.0.0.1:8080/");
var statusLabel = document.getElementById("statusLabel");

socket.onopen = function() {
	statusLabel.innerHTML = "Connected!";
	statusLabel.style.color = "#009933";
};

function ConnectionFailure() {
	statusLabel.innerHTML = "Connection failed!";
	statusLabel.style.color = "#FF0000";
}

socket.onmessage = function(msg) {
	alert("Message recieved: " + msg.data);
};

socket.onclose = ConnectionFailure;
socket.onerror = ConnectionFailure;