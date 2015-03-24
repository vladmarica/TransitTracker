function initMap()
{
	var mapOptions = {
    	zoom: 14,
    	center: new google.maps.LatLng(45.961, -66.643),
    	disableDefaultUI: true
  	};

  	var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

  	var marker = new google.maps.Marker({
    	position: new google.maps.LatLng(45.961, -66.643),
    	title: "Bus 12"
	});

	marker.setMap(map);

	console.log("Map initialized!");
}

window.onload = function()
{
	var script = document.createElement('script');
  	script.type = 'text/javascript';
  	script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCXi20azot7i4AjP1B3p4wydRKn7HbquLo&callback=initMap';
  	document.body.appendChild(script);
};