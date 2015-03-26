var table = document.getElementById("bus-table");
var buses = [];

function UpdateTable(data)
{
	var row = document.getElementById("bus" + data.id);
	var idCell = document.getElementById("bus" + data.id + "_id");
	var updateCell = document.getElementById("bus" + data.id + "_updatetime");

	if (!row) {
		row = table.insertRow(-1);
		idCell = row.insertCell(0);
		updateCell = row.insertCell(1);
	}

	idCell.innerHTML = data.id;
	updateCell.innerHTML = "0 Seconds Ago";


}

function UpdateBus(data)
{
	var tab = {};
	if (!buses.contains(data.id)) {
		buses[data.id] = tab;
	}	
	else {
		tab = buses[data.id];
	}

	tab.lastUpdateTime = 0;
	tab.id = data.id;

	console.log("Updated bus with ID " + tab.id);
	console.log(tab);

	UpdateTable(tab);
}

var interv = setInterval(function()
{
	console.log("Update");
}, 1000);