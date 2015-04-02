var map;

function initMap()
{
	var mapOptions = {
		zoom: 14,
    	center: new google.maps.LatLng(45.961, -66.643),
    	disableDefaultUI: true,
      minZoom: 12,
      maxZoom: 17
    };

    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    console.log("Map initialized!");
}

window.onload = function()
{
	/*
	var script = document.createElement('script');
	script.type = 'text/javascript';
  	script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCXi20azot7i4AjP1B3p4wydRKn7HbquLo&callback=initMap';
  	document.body.appendChild(script);

  	var script2 = document.createElement('script');
	script2.type = 'text/javascript';
  	script2.src = 'scripts/buses.js';
  	document.body.appendChild(script2);
  	*/

  	initMap();
  	StartSocket();
}