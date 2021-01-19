var jwt;
var id;
var userLogin;


$(document).ready(function () {
    cookie_array= document.cookie.split("&");
    var temp = cookie_array[0].trim();
    jwt = temp.substring("access_token=".length,temp.length);
    var temp = cookie_array[2].trim();
    id = temp.substring("id=".length,temp.length);
    
    $("#logOut").click(function(){
        document.cookie='access_token= & role= & id= ;';
        window.location.replace('index.html'); 
    })
    $.ajax({
        url: "http://192.168.160.217:8080/api/patients/"+ id,
        headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
        statusCode: {
            500: function(xhr){
                return;
            },
            403: function(xhr){
                return;
            }
        }
    }).then(function(user) {
        console.log(user)
        userLogin=user
    
        $("#latestInf").fadeOut();

        // Add content to HTML
        $("#userFullName").text(userLogin['fullname']);
        $("#userId").text(userLogin['username']);
        $("#userEmail").text(userLogin['email']);
        $("#userAge").text(userLogin['age']);
        if (userLogin['gender']== "Male"){
            $("#userGender").text("Male");
        }
        else{
            $("#userGender").text("Female");
        }
        $("#userWeight").text(userLogin['weight']);
        $("#userHeight").text(userLogin['height']);
        $("#userPhoto").attr("src",'data:image/gif;base64,'+ userLogin['image']);
        var condArray = userLogin['med_conditions'];
        $.each(condArray, function(index, value) {
            //console.log(value);
            $("#userConditionList").append("<li>" + value + "</li>");
        });

        var medicationArray = userLogin['medication'];
        $.each(medicationArray, function(index, value) {
            //console.log(value);
            $("#userMedicationList").append("<li>" + value + "</li>");
        });

        // Functionality not implemented yet
        $(".notImplemented").click(function () {
            alert("Sorry, but this functionality has not been implemented yet! :(");
        });

        $("#latestInfo").click(function(){
            $("#latestInf").fadeToggle("slow");
            get_latestValues();
        })

        get_latestValues();
        loadCharts();
    });
});

// LATEST DATA
function get_latestValues(){
    $.ajax({
        url: "http://192.168.160.217:8080/api/patients/"+ id+'/latest',
        headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
        }).done(function (results) {
            console.log(results)
            document.getElementById('latest_bp').innerHTML = '-> '+ (results[0].low_value).toFixed(2) + ' | ' + (results[0].high_value).toFixed(2);
            document.getElementById('latest_bt').innerHTML = '-> '+ (results[1].bodyTemp).toFixed(2);
            document.getElementById('latest_hr').innerHTML = '-> '+ results[2].heartRate;
            document.getElementById('latest_sl').innerHTML = '-> '+ (results[3].sugarLevel).toFixed(2);
            document.getElementById('latest_ol').innerHTML = '-> '+ (results[4].oxygenLevel).toFixed(2);
        })
}

function loadCharts(){

    // Heart Rate
    google.charts.load('current', {packages: ['line']});
    google.charts.setOnLoadCallback(function() { draw_HeartRateChart("D");});

    // Blood Pressure
    google.charts.load('current', {'packages':['bar']});
    google.charts.setOnLoadCallback(function() { draw_BloodPressureChart("W");});

    // Temperatue
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(function() { draw_TemperatureChart("W");});

    // Blood Sugar
    google.charts.load('current', {packages: ['corechart', 'bar']});
    google.charts.setOnLoadCallback(function() { draw_BloodSugarChart("W")});

    // Oxygen Saturation
    google.charts.load('current', {packages: ['corechart', 'bar']});
    google.charts.setOnLoadCallback(function() { draw_OxygenSaturationChart("D");});

}
// CHARTS

// Heart Rate
function draw_HeartRateChart(int_date) {
    var start_date;
    var end_date= new Date().toISOString()
    results=[]
    if(int_date=="D"){
        start_date = new Date(Date.now() - 86400 * 1000).toISOString()
    }
    else{
        start_date = new Date(Date.now() - 86400*7 * 1000).toISOString()
    }
    i_page=0;
    pcount =0;
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/patients/"+ id+"/heartrate?start_date="+start_date+"&end_date="+end_date+"&page="+i_page,
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
            dataType: 'json',
            async: false
         }).done(function (r) {
            pcount= r.totalPages;
            results=results.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);
    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Day');
    data.addColumn('number', 'Beats Per Minute');
    // Get users data
    results.forEach(elem =>{
        var date = elem['date'].split("-");
        var hours=date[2].split("T")[1].split(":")
        var value = elem['heartRate'];
        //console.log(date, value);
        data.addRows([
            [new Date(date[0], date[1]-1, date[2].split("T")[0],hours[0],hours[1]), value]
        ]);
    })
    
    var options = {
        title: 'Resting Heart Rate',
        curveType: 'function',
        height: 350,
        hAxis: { title: 'Time' },
        vAxis: { title: 'Heart Raten in BPM' },
        legend: { position: "none" },
        tooltip: {isHtml: true}
    };
    
    var chart = new google.charts.Line(document.getElementById('heartrate_chart'));
    chart.draw(data, google.charts.Line.convertOptions(options));
}


function draw_BloodPressureChart(int_date) {
    var start_date;
    var end_date= new Date().toISOString()
    results=[]
    if(int_date=="W"){
        start_date = new Date(Date.now() - 86400* 7 * 1000).toISOString()
    }
    else{
        start_date = new Date(Date.now() - 86400* 30 * 1000).toISOString()
    }
    i_page=0;
    pcount =0;
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/patients/"+ id+"/bloodpressure?start_date="+start_date+"&end_date="+end_date+"&page="+i_page,
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
            dataType: 'json',
            async: false
         }).done(function (r) {
            pcount= r.totalPages;
            results=results.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);
    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Day');
    data.addColumn('number', 'Diastolic, mm Hg');
    data.addColumn('number', 'Sistolic, mm Hg');
    // Get users data
    results.forEach(elem =>{
        var date = elem['date'].split("-");
        var high_value = elem['high_value'];
        var low_value = elem['low_value'];
        //console.log(date, value_diast, value_sys);
        data.addRows([
            [new Date(date[0], date[1]-1, date[2].split("T")[0],date[2].split("T")[1].split(":")[0]), low_value, high_value]
        ]);
    })

    var options = {
        chart: {
            title: 'Blood Pressure',
            subtitle: 'Diastolic'
        },
        height: 350,
        hAxis: { 
            title: 'Time',
            minValue: start_date,
            maxValue:end_date
        },
        vAxis: { title: 'Blood Pressure, in mm Hg' },
        legend: { position: "top" },
    };
    
    var chart = new google.charts.Bar(document.getElementById('bloodpressure_chart'));
    chart.draw(data, google.charts.Bar.convertOptions(options));

}


function draw_TemperatureChart(int_date) {
    var start_date;
    var end_date= new Date().toISOString()
    results=[]
    if(int_date=="W"){
        start_date = new Date(Date.now() - 86400* 7 * 1000).toISOString()
    }
    else{
        start_date = new Date(Date.now() - 86400* 30 * 1000).toISOString()
    }
    i_page=0;
    pcount =0;
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/patients/"+ id+"/bodytemperature?start_date="+start_date+"&end_date="+end_date+"&page="+i_page,
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
            dataType: 'json',
            async: false
         }).done(function (r) {
            pcount= r.totalPages;
            results=results.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Day');
    data.addColumn('number', 'C*');

    // Get users data
    results.forEach(element =>{
        var date = element['date'].split("-");
        var value = element['bodyTemp'];
        //console.log(date, value);
        data.addRows([
            [new Date(date[0], date[1]-1, date[2].split("T")[0]), value]
        ]);
    })

    var options = {
        title: 'Body Temperature',
        curveType: 'function',
        height: 350,
        hAxis: { title: 'Time' },
        vAxis: { title: 'Temperature, in C*' },
        legend: { position: "none" },
        tooltip: {isHtml: true}
    };

    var chart = new google.charts.Line(document.getElementById('bodytemperature_chart'));
    chart.draw(data, google.charts.Line.convertOptions(options));
}


function draw_BloodSugarChart(int_date) {
    var start_date;
    var end_date= new Date().toISOString()
    results=[]
    if(int_date=="W"){
        start_date = new Date(Date.now() - 86400* 7 * 1000).toISOString()
    }
    else{
        start_date = new Date(Date.now() - 86400* 30 * 1000).toISOString()
    }
    i_page=0;
    pcount =0;
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/patients/"+ id+"/sugarlevel?start_date="+start_date+"&end_date="+end_date+"&page="+i_page,
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
            dataType: 'json',
            async: false
         }).done(function (r) {
            pcount= r.totalPages;
            results=results.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);
    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Day');
    data.addColumn('number', 'mg/dL');

    // Get users data
    results.forEach(element =>{
        var date = element['date'].split("-");
        var value = element['sugarLevel'];
        //console.log(date, value);
        data.addRows([
            [new Date(date[0], date[1]-1, date[2].split("T")[0]), value]
        ]);
    })

    var options = {
        title: "Blood Glucose Level",
        height: 350,
        hAxis: { title: 'Time' },
        vAxis: { title: 'Blood Glucose, in mg/dL' },
        legend: { position: "none" },
        tooltip: {isHtml: true}
    };
    var chart = new google.charts.Bar(document.getElementById('bloodglucose_chart'));
    chart.draw(data, google.charts.Bar.convertOptions(options));
}


function draw_OxygenSaturationChart(int_date) {
    var start_date;
    var end_date= new Date().toISOString()
    
    results=[]
    if(int_date=="D"){
        start_date = new Date(Date.now() - 86400* 1000).toISOString()
    }
    else{
        start_date = new Date(Date.now() - 86400* 7 * 1000).toISOString()
    }
    i_page=0;
    pcount =0;
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/patients/"+ id+"/oxygenlevel?start_date="+start_date+"&end_date="+end_date+"&page="+i_page,
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
            dataType: 'json',
            async: false
         }).done(function (r) {
            pcount= r.totalPages;
            results=results.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Day');
    data.addColumn('number', '%');

    // Get users data
    results.forEach(element =>{
        var date = element['date'].split("-");
        var value = element['oxygenLevel'];
        var hours=date[2].split("T")[1].split(":")
        //console.log(date, value);
        data.addRows([
            [new Date(date[0], date[1]-1, date[2].split("T")[0],hours[0],hours[1]), value]
        ]);
    })

    var options = {
        title: "Oxygen Saturation",
        height: 350,
        hAxis: { title: 'Time' },
        vAxis: { title: 'Oxygen Saturation, in %' },
        legend: { position: "none" },
        tooltip: {isHtml: true}
    };
    var chart = new google.charts.Bar(document.getElementById('oxygensaturation_chart'));
    chart.draw(data, google.charts.Bar.convertOptions(options));
}

