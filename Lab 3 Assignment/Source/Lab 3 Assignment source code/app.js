var myApp = angular.module('myApp', ['ngRoute']);

myApp.config(function($routeProvider,$httpProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'login.html',
            controller: 'loginController'
        })
        .when('/register', {
            templateUrl: 'register.html',
            controller: 'registerController'
        })
        .when('/home', {
            templateUrl: 'home.html',
            controller: 'homeController'
        });

});

myApp.config(function($sceDelegateProvider) {
    $sceDelegateProvider.resourceUrlWhitelist([
        'self',
        'https://www.youtube.com/**'
    ]);
});
myApp.controller('loginController', function($scope) {
    $scope.pageClass = 'login';
    $scope.FBlogin= function () {

        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) return;
            js = d.createElement(s); js.id = id;
            js.src = "//connect.facebook.net/en_US/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));

        window.fbAsyncInit = function() {
            FB.init({
                appId      : '642782199230310',
                cookie     : true,  // enable cookies to allow the server to access
                                    // the session
                xfbml      : true,  // parse social plugins on this page
                version    : 'v2.5' // use graph api version 2.5
            });

            FB.login(function(response) {
                if (response.authResponse) {
                    console.log('Welcome!  Fetching your information.... ');
                    FB.api('/me', function(response) {
                        console.log('Good to see you, ' + response.name + '.');
                        window.location = '#home';
                    });
                } else {
                    console.log('User cancelled login or did not fully authorize.');
                }
            });
            // Now that we've initialized the JavaScript SDK, we call
            // FB.getLoginStatus().  This function gets the state of the
            // person visiting this page and can return one of three states to
            // the callback you provide.  They can be:
            //
            // 1. Logged into your app ('connected')
            // 2. Logged into Facebook, but not your app ('not_authorized')
            // 3. Not logged into Facebook and can't tell if they are logged into
            //    your app or not.
            //
            // These three cases are handled in the callback function.

            FB.getLoginStatus(function(response) {
                console.log(response.status);
            });
        };
    };

});

myApp.controller('registerController', function($scope) {
    $scope.pageClass = 'register';
});

myApp.controller('homeController', function($scope,$http,$sce) {
    $scope.pageClass = 'home';
    $scope.videoIdList = new Array();
    $scope.searchVideo =function () {
        var search=document.getElementById("searchKey").value;
        var speakup = "Searching top 5 trending video in web for "+search;
        console.log(speakup);
        var msg = new SpeechSynthesisUtterance(speakup);
        window.speechSynthesis.speak(msg);
        var request = gapi.client.youtube.search.list({
            part: 'snippet',
            type: 'video',
            q: search
        });
        var vid;
        var YTurl1;

        var response=$http.get("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=5&q="+search+"&type=video&key=AIzaSyD_d_XWDzXLeHfv9vPSDzaIZMQaas_olsw");
        response.success(function (data) {
            console.log("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=5&q="+search+"&type=video&key=AIzaSyD_d_XWDzXLeHfv9vPSDzaIZMQaas_olsw")
            for (var i = 0; i < 5; i++) {
                var filter = data.items[i].id.videoId;
                $scope.videoIdList[i] = {
                    "vid": data.items[i].id.videoId,
                    "vurl": 'https://www.youtube.com/embed/'+filter
                };
            }

            /* var item = data.items[0].id.videoId;
             console.log(item);
             $scope.YTURL = $sce.trustAsResourceUrl('https://www.youtube.com/embed/'+item);*/
        });
        console.log(response);
    };
    $scope.getTransaltion = function () {
        var search=document.getElementById("searchKey").value;
        //$scope.translateValue = "";
        if (searchKey != null && searchKey != "") {
            //  document.getElementById('div_ReviewList').style.display = 'none';
            //This is the API that gives the list of venues based on the place and search query.
            var handler = $http.get("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20151023T145251Z.bf1ca7097253ff7e.c0b0a88bea31ba51f72504cc0cc42cf891ed90d2&text="+search+"&lang=en-hi&[format=plain]&[options=1]&[callback=set]");
            handler.success(function (data) {

                if (data != null ) {

                    $scope.translateValue = data.text;
                }

            });
            handler.error(function (data) {
                alert("There was some error processing your request. Please try after some time.");
            });
        }
    };
});
function init () {
    gapi.client.setApiKey("AIzaSyD_d_XWDzXLeHfv9vPSDzaIZMQaas_olsw");
    gapi.client.load('youtube', 'v3', function() {
        //
    });
};