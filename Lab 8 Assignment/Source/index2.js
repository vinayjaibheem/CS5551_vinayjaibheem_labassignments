function getCredentials(callbackFunction) {
    var data = {
        'grant_type': 'client_credentials',
        'client_id': CLIENT_ID,
        'client_secret': CLIENT_SECRET

    };
    var url = 'https://api.clarifai.com/v1/token';

    return axios.post(url, data, {
        'transformRequest': [
            function() {
                return transformDataToParams(data);
            }
        ]
    }).then(function(r) {
        localStorage.setItem('accessToken', r.data.access_token);
        localStorage.setItem('tokenTimestamp', Math.floor(Date.now() / 1000));
        // yolo
        callbackFunction();

    }, function(err) {
        console.log(err);
    });
}

function transformDataToParams(data) {
    var str = [];
    for (var p in data) {
        if (data.hasOwnProperty(p) && data[p]) {
            if (typeof data[p] === 'string'){
                str.push(encodeURIComponent(p) + '=' + encodeURIComponent(data[p]));
            }
            if (typeof data[p] === 'object'){
                for (var i in data[p]) {
                    str.push(encodeURIComponent(p) + '=' + encodeURIComponent(data[p][i]));
                }
            }
        }
    }
    return str.join('&');
}
function postImage(imgurl) {
    var accessToken = localStorage.getItem('accessToken');

    var data = {
        'url': imgurl
    };
    var url = 'https://api.clarifai.com/v1/tag';
    return axios.post(url, data, {
        'headers': {
            'Authorization': 'Bearer ' + accessToken
        }
    }).then(function(r) {
        // PARSE HERE
        parseResponse(r.data);

    }, function(err) {
        console.log('Sorry, something is wrong: ' + err);
    });
}
function parseResponse(resp) {
    var tags = [];
    if (resp.status_code === 'OK') {
        var results = resp.results;
        tags = results[0].result.tag.classes;
    } else {
        console.log('Sorry, something is wrong.');
    }
    var hello =document.getElementById('tags').innerHTML = tags.toString().replace(/,/g, ', ');
    var hello1 = hello.toString().replace(/, /g, ' %20 ');
    var xmlhttp = new XMLHttpRequest();
    var url = "https://api.uclassify.com/v1/uclassify/topics/classify/?readKey=zp3PVGoy6XnZ&text="+hello1;
    ur= "https://api.uclassify.com/v1/uclassify/topics/classify/?readKey=zp3PVGoy6XnZ&text="+hello1;
    if(localStorage){localStorage.setItem('ur',hello1)}
    xmlhttp.onreadystatechange=function() {
     if (this.readyState == 4 && this.status == 200) {
     myFunction(this.responseText);
     }
     }
     xmlhttp.open("GET", url, true);
     xmlhttp.send();
     function myFunction(response) {
     var obj = JSON.parse(response);
         document.getElementById("demo").innerHTML ="Arts "+ obj.Arts +
             "  Business " + obj.Business +"  ,Computers "+ obj.Computers + "  ,Games "+ obj.Games +
             "  ,Health "+ obj.Health + "  ,Home "+ obj.Home + "  ,Recreation "+ obj.Recreation +
             "  ,Science "+ obj.Science + "  ,Society "+ obj.Society + "  ,Sports "+ obj.Sports;}
   /* if (localStorage) {
        Art= obj.Arts;
        Bus= obj.Business;
        Com= obj.Computers;
        Gam= obj.Games;
        Hel= obj.Health;
        Hom= obj.Home;
        Rec= obj.Recreation;
        Sci= obj.Science;
        Soc= obj.Society;
        Spo= obj.Sports;
        localStorage.setItem('Ar',Art);
        localStorage.setItem('Bu',Bus);
        localStorage.setItem('Ar',Com);
        localStorage.setItem('Bu',Gam);
        localStorage.setItem('Ar',Hel);
        localStorage.setItem('Bu',Hom);
        localStorage.setItem('Ar',Rec);
        localStorage.setItem('Bu',Sci);
        localStorage.setItem('Ar',Soc);
        localStorage.setItem('Bu',Spo);
    }*/
     }
function run(imgurl) {
    if (Math.floor(Date.now() / 1000) - localStorage.getItem('tokenTimeStamp') > 86400 || localStorage.getItem('accessToken') === null) {
        getCredentials(function() {
            postImage(imgurl);
        });
    } else {
        postImage(imgurl);
    }
}