var table = document.getElementById("bus-table");
var buses = [];

var colors = ["F7685C", "F7D25C", "5C86F7", "A254DF", "696969", "CC3531", "A254DF", "31C8CC", "54DF61"];
var nextColorIndex = 0;

function UpdateTable(data)
{
	var row = document.getElementById("bus" + data.id);
	var idCell = document.getElementById("bus" + data.id + "_id");
	var updateCell = document.getElementById("bus" + data.id + "_updatetime");
	

	if (!row) {
		row = table.insertRow(-1);
		idCell = row.insertCell(0);
		updateCell = row.insertCell(1);

		button = document.createElement("button");
		button.className = "link";
  		button.innerHTML = "<b>" + data.id + "</b>";
  		idCell.appendChild(button);

  		button.onclick = function()
  		{
  			var marker = buses[data.id].marker;
  			map.setCenter(marker.getPosition());
  			map.setZoom(17);
  		}

  		//color cell
  		var colorCell = row.insertCell(2);
  		var rect = document.createElement("div");  		
  		//rect.style = "width:100%;height:10px;background:blue;";

  		colorCell.appendChild(rect);
  		rect.style.background = "#" + data.color;
  		rect.style.height = "22px"
	}

	var button = idCell.firstChild;

	row.id = "bus" + data.id;
	idCell.id = "bus" + data.id + "_id";
	updateCell.id = "bus" + data.id + "_updatetime";

	updateCell.innerHTML = data.lastUpdateTime + " Seconds Ago";

	if ("interval_id" in data) {
		clearInterval(data.interval_id);
	}

	data.interval_id = setInterval(function() {
		data.lastUpdateTime++;
		updateCell.innerHTML = data.lastUpdateTime + " Seconds Ago";
	}, 1000);
}

function UpdateBus(data)
{
	var tab = {};

	if (buses[data.id] == undefined) {
		buses[data.id] = tab;
		tab.color = colors[nextColorIndex++];
		if (nextColorIndex == colors.length) {
			nextColorIndex = 0;
		}

		//marker image
		var image = {
   			url: 'markers/' + tab.color + '.png',
    		size: new google.maps.Size(22, 40),
    		origin: new google.maps.Point(0,0),
    		anchor: new google.maps.Point(11, 40)
  		};

  		//create the market
		tab.marker = new google.maps.Marker({
    		position: new google.maps.LatLng(data.degreesNorth, data.degreesWest),
    		title: "Bus " + data.id,
    		icon: image
    	});

    	tab.marker.setMap(map);
    	tab.lastUpdateTime = data.lastUpdateTime
	}	
	else {
		tab = buses[data.id];
		AnimateMarker(tab.marker, data.degreesNorth, data.degreesWest);
		tab.lastUpdateTime = 0;
	}

	tab.id = data.id;

	UpdateTable(tab);
}

function AnimateMarker(marker, newLat, newLng)
{
	var oldPos = marker.getPosition();
	var oldLat = oldPos.lat();
	var oldLng = oldPos.lng();

	var deltaLat = newLat - oldLat;
	var deltaLng = newLng - oldLng;

	var latIncr = deltaLat / 20;
	var lngIncr = deltaLng / 20;

	var loops = 0;
	var animationID;

	animationID = setInterval(function() {
		loops++;

		var newPos = new google.maps.LatLng(oldLat + (latIncr * loops), oldLng + (lngIncr * loops));
		marker.setPosition(newPos);
		
		if (loops == 20) {
			clearInterval(animationID);
		}
	}, 50);
}