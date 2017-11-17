angular.module('MyModule',[])
    .controller('MyController', function ($scope,$http) {

        $scope.retrieveData = function (){
            document.getElementById('results').style.visibility="visible";
            var query = document.getElementById('query').value;
            $scope.query=query;
            var resp=$http.get("https://api.foursquare.com/v2/venues/search?ll=36.7782,-119.4179&query="+query+"&limit=4&client_id=JSSILYLBIMN1X42MMLDIJNC0GHFIWQL1GHNSMY4RCLENA5VA&client_secret=1O1OZKKOWKGGHHBKY3ZW4PIIRS1GVLI23MK0HYWABBEBNYFA&v=20130815");
            resp.success(function(data,status, headers,config){
                $scope.name0 = data.response.venues[0].name;
                $scope.name1 = data.response.venues[1].name;
                $scope.name2 = data.response.venues[2].name;
               /* $scope.name3 = data.response.venues[3].name;
                $scope.name4 = data.response.venues[4].name;
                $scope.name5 = data.response.venues[5].name;*/
                $scope.address0 = data.response.venues[0].location.address+" ("+data.response.venues[0].location.crossStreet+"), "+data.response.venues[0].location.cc+", "+data.response.venues[0].location.city+", "+data.response.venues[0].location.state+", "+data.response.venues[0].location.postalCode+", "+data.response.venues[0].location.country+", ";
                $scope.contact0 = data.response.venues[0].contact.formattedPhone;
                $scope.address1 = data.response.venues[1].location.address+" ("+data.response.venues[1].location.crossStreet+"), "+data.response.venues[1].location.cc+", "+data.response.venues[1].location.city+", "+data.response.venues[1].location.state+", "+data.response.venues[1].location.postalCode+", "+data.response.venues[1].location.country+", ";
                $scope.contact1 = data.response.venues[1].contact.formattedPhone;
                $scope.address2 = data.response.venues[2].location.address+" ("+data.response.venues[2].location.crossStreet+"), "+data.response.venues[2].location.cc+", "+data.response.venues[2].location.city+", "+data.response.venues[2].location.state+", "+data.response.venues[2].location.postalCode+", "+data.response.venues[2].location.country+", ";
                $scope.contact2 = data.response.venues[2].contact.formattedPhone;
               /* $scope.address3 = data.response.venues[3].location.address+" ("+data.response.venues[3].location.crossStreet+"), "+data.response.venues[3].location.cc+", "+data.response.venues[3].location.city+", "+data.response.venues[3].location.state+", "+data.response.venues[3].location.postalCode+", "+data.response.venues[3].location.country+", ";
                $scope.contact3 = data.response.venues[3].contact.formattedPhone;
                $scope.address4 = data.response.venues[4].location.address+" ("+data.response.venues[4].location.crossStreet+"), "+data.response.venues[4].location.cc+", "+data.response.venues[4].location.city+", "+data.response.venues[4].location.state+", "+data.response.venues[4].location.postalCode+", "+data.response.venues[4].location.country+", ";
                $scope.contact4 = data.response.venues[4].contact.formattedPhone;
                $scope.address5 = data.response.venues[5].location.address+" ("+data.response.venues[5].location.crossStreet+"), "+data.response.venues[5].location.cc+", "+data.response.venues[5].location.city+", "+data.response.venues[5].location.state+", "+data.response.venues[5].location.postalCode+", "+data.response.venues[5].location.country+", ";
                $scope.contact5 = data.response.venues[5].contact.formattedPhone;*/
            });
            resp.error(function(data,status,headers, config){
                window.alert("response not received 1, Something went wrong");
            });
        }
        $scope.calcWeather = function (){
            document.getElementById('weather-canvas').style.visibility="visible";
            document.getElementById('weatherheader').style.visibility="visible";
            var resp=$http.get("http://api.openweathermap.org/data/2.5/weather?q=california&units=imperial&appid=ba46ee733b337f4d3e99cdd312b0c51e");
            resp.success(function(data,status, headers,config){
                $scope.sourcetemp = data.main.temp+"` F";
                $scope.sourcedes=data.weather[0].main;
               /* $scope.sourcepre=data.main.pressure+" hPa";
                $scope.sourcehum=data.main.humidity+" %";*/
                $scope.sourcewind=data.wind.speed+" miles/hour";
                console.log("data :"+ $scope.sourcetemp);
            });
            resp.error(function(data,status,headers, config){
                window.alert("response not received 1, Something went wrong");
            });
        }

    });
