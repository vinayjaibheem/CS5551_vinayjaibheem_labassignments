var request = require('request');
request('https://maps.googleapis.com/maps/api/place/search/json?location=39.0997,-94.5786&radius=1000&type=restaurant&sensor=true&key=AIzaSyCXpd1MNs44B5NJ5xs2PsTDeGFjlXC8ORw', function (error, response, body) {
    //Check for error
    if(error){
        return console.log('Error:', error);
    }

    //Check for right status code
    if(response.statusCode !== 200){
        return console.log('Invalid Status Code Returned:', response.statusCode);
    }
//	console.log(body);
    //All is good. Print the body
    body = JSON.parse(body);
	var res = body.response.results;
	var i;
	for(i=0;i<res.length;i++)
	{
		console.log(res[i].name);
	}
	
});