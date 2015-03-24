var table = document.getElementById("bus-table");
var buses = [];

function UpdateTable()
{
	var row = document.getElementById("bus ")
}

function UpdateBus(data)
{
	var tab = {};
	if (!buses.contains(data.id)) {
		buses[data.id] = table;
	}	
	else {
		tab = buses[data.id];
	}

	tab.lastUpdateTime = 0;
	console.log("Updated bus with ID " + data.id);
}

UpdateBus({
	id: 42,
});