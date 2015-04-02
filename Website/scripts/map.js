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
  	initMap();
  	StartSocket();
}