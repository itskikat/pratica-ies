var jwt;
var id;

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


});


google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(draw_StateChart);
function draw_StateChart() {
    i_page=0;
    pcount =0;
    var patients =[];
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients?page=" + i_page,
            //url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients",
            //headers:{"Access-Control-Allow-Origin":"http://192.168.160.217:8080"},
            headers:{
                "Access-Control-Allow-Origin":"http://192.168.160.217",
                "Authorization": "Bearer " + jwt
            },
            async: false,
            statusCode: {
                500: function(xhr){
                    console.log("There was an error connecting to the server, please try again!");
                    return;
                },
                403: function(xhr){
                    console.log("Invalid Credentials!");
                    return;
                }
            }
         }).done(function (r) {
            pcount= r.totalPages;
            patients=patients.concat(r.data);
            i_page+=1;
         })
    } while (i_page < pcount);

    data_array = [['State', 'Number of Patients']];
    states_array = [];
    patients.forEach(p => {
        var currentState=p['currentstate'];
        states_array.push(currentState);
    });

    var counts = {};
    states_array.forEach(function(x) { counts[x] = (counts[x] || 0)+1; });  

    for (const [key, value] of Object.entries(counts)) {
        data_array.push([key, value]);
    }

    var data = google.visualization.arrayToDataTable(data_array);

    var options = {
        title: 'Patients by State',
        height: 550,
        width: 550
    };

    var chart = new google.visualization.PieChart(document.getElementById('state_chart'));

    chart.draw(data, options);
}


google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(draw_AgeChart);
function draw_AgeChart() {

    i_page=0;
    pcount =0;
    var patients =[];
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients?page=" + i_page,
            //url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients",
            //headers:{"Access-Control-Allow-Origin":"http://192.168.160.217:8080"},
            headers:{
                "Access-Control-Allow-Origin":"http://192.168.160.217",
                "Authorization": "Bearer " + jwt
            },
            async: false,
            statusCode: {
                500: function(xhr){
                    console.log("There was an error connecting to the server, please try again!");
                    return;
                },
                403: function(xhr){
                    console.log("Invalid Credentials!");
                    return;
                }
            }
         }).done(function (r) {
            pcount= r.totalPages;
            patients=patients.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);

    data_array = [['Age', 'Number of Patients']];
    ages = {'<20':0, '20-39':0, '40-59':0, '60-69':0, '70-79':0, '80-89':0, '90+':0}
    patients.forEach(p => {
        var age=p['age'];
        if (age<20) {
            ages['<20']++
        } else if (age<40) {
            ages['20-39']++
        } else if (age<60) {
            ages['40-59']++
        } else if (age<70) {
            ages['60-69']++
        } else if (age<80) {
            ages['70-79']++
        } else if (age<90) {
            ages['80-89']++
        } else{
            ages['90+']++
        }
    });

    for (const [key, value] of Object.entries(ages)) {
        data_array.push([key, value]);
    }

    var data = google.visualization.arrayToDataTable(data_array);

    var options = {
        title: 'Patients by Age',
        height: 550,
        width: 550
    };

    var chart = new google.visualization.PieChart(document.getElementById('age_chart'));

    chart.draw(data, options);

}


google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(draw_CheckChart);
function draw_CheckChart() {
    i_page=0;
    pcount =0;
    var patients =[];
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients?page=" + i_page,
            //url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients",
            //headers:{"Access-Control-Allow-Origin":"http://192.168.160.217:8080"},
            headers:{
                "Access-Control-Allow-Origin":"http://192.168.160.217",
                "Authorization": "Bearer " + jwt
            },
            async: false,
            statusCode: {
                500: function(xhr){
                    console.log("There was an error connecting to the server, please try again!");
                    return;
                },
                403: function(xhr){
                    console.log("Invalid Credentials!");
                    return;
                }
            }
         }).done(function (r) {
            pcount= r.totalPages;
            patients=patients.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);


    data_array = [['Date', 'Number of Patients']];
    dates = {'Today':0, 'This Week':0, 'Last 2 Weeks':0, 'This Month':0, 'More than a month ago':0}
    patients.forEach(p => {
        var date=Date.now()- new Date(p['lastCheck']);
        if (date<86400000) {
            dates['Today']++
        } else if (date<604800000) {
            dates['This Week']++
        } else if (date<1209600000) {
            dates['Last 2 Weeks']++
        } else if (date<2592000000) {
            dates['This Month']++
        } else{
            dates['More than a month ago']++
        }
    });

    for (const [key, value] of Object.entries(dates)) {
        data_array.push([key, value]);
    }

    var data = google.visualization.arrayToDataTable(data_array);

    var options = {
        title: 'Patients by Last Checked',
        height: 550,
        width: 550
    };

    var chart = new google.visualization.PieChart(document.getElementById('check_chart'));

    chart.draw(data, options);

}


google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(draw_HealthChart);
function draw_HealthChart() {

    i_page=0;
    pcount =0;
    var patients =[];
    do {
        $.ajax({
            url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients?page=" + i_page,
            //url: "http://192.168.160.217:8080/api/professionals/" +  id + "/patients",
            //headers:{"Access-Control-Allow-Origin":"http://192.168.160.217:8080"},
            headers:{
                "Access-Control-Allow-Origin":"http://192.168.160.217",
                "Authorization": "Bearer " + jwt
            },
            async: false,
            statusCode: {
                500: function(xhr){
                    console.log("There was an error connecting to the server, please try again!");
                    return;
                },
                403: function(xhr){
                    console.log("Invalid Credentials!");
                    return;
                }
            }
         }).done(function (r) {
            pcount= r.totalPages;
            patients=patients.concat(r.data);
            i_page+=1;
         })
    } while (i_page<pcount);


    bph_array = [['Systolic Blood Pressure', 'Number of Patients']];
    bph_vals = {'<99':0, '100-119':0, '120-129':0, '130-139':0, '140-149':0, '150+':0}

    bpl_array = [['Diastolic Blood Pressure', 'Number of Patients']];
    bpl_vals = {'<64':0, '65-79':0, '80-89':0, '90-109':0, '110-119':0, '120+':0}

    sl_array = [['Sugar Level', 'Number of Patients']];
    sl_vals = {'<3':0, '3-5.5':0, '5.5-7':0, '7-9':0, '9+':0}

    ol_array = [['Oxygen Saturation', 'Number of Patients']];
    ol_vals = {'<89':0, '90-94':0, '95-100':0}

    bt_array = [['Body Temperature', 'Number of Patients']];
    bt_vals = {'<35':0, '35-37.5':0, '37.5-38.3':0, '38.3+':0}

    hr_array = [['Heart Rate', 'Number of Patients']];
    hr_vals = {'<75':0, '75-84':0, '85-99':0, '100-119':0, '120+':0}
    patients.forEach(p => {
        
        $.ajax({
            async: false,
            url: 'http://192.168.160.217:8080/api/patients/'+p['id']+'/latest',
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
            }).done(function (results) {
    
                var bpl = results[0].low_value;
                var bph = results[0].high_value
                var bt = results[1].bodyTemp;
                var hr = results[2].heartRate;
                var sl = results[3].sugarLevel;
                var ol = results[4].oxygenLevel;

                if (bpl<64) {
                    bpl_vals['<64']++
                } else if (bpl<80) {
                    bpl_vals['65-79']++
                } else if (bpl<90) {
                    bpl_vals['80-89']++
                } else if (bpl<110) {
                    bpl_vals['90-109']++
                } else if (bpl<120) {
                    bpl_vals['110-119']++
                } else{
                    bpl_vals['120+']++
                }

                if (bph<99) {
                    bph_vals['<99']++
                } else if (bph<120) {
                    bph_vals['100-119']++
                } else if (bph<130) {
                    bph_vals['120-129']++
                } else if (bph<140) {
                    bph_vals['130-139']++
                } else if (bph<150) {
                    bph_vals['140-149']++
                } else{
                    bph_vals['150+']++
                }

                if (bt<35) {
                    bt_vals['<35']++
                } else if (bt<37.5) {
                    bt_vals['35-37.5']++
                } else if (bt<38.3) {
                    bt_vals['37.5-38.3']++
                } else{
                    bt_vals['38.3+']++
                }

                if (sl<3) {
                    sl_vals['<3']++
                } else if (sl<5.5) {
                    sl_vals['3-5.5']++
                } else if (sl<7) {
                    sl_vals['5.5-7']++
                } else if (sl<9) {
                    sl_vals['7-9']++
                } else{
                    sl_vals['9+']++
                }

                if (ol<89) {
                    ol_vals['<89']++
                } else if (ol<95) {
                    ol_vals['90-94']++
                } else{
                    ol_vals['95-100']++
                }

                if (hr<75) {
                    hr_vals['<75']++
                } else if (hr<85) {
                    hr_vals['75-84']++
                } else if (hr<100) {
                    hr_vals['85-99']++
                } else if (hr<120) {
                    hr_vals['100-119']++
                } else{
                    hr_vals['120+']++
                }

            })

    });

    for (const [key, value] of Object.entries(bpl_vals)) {
        bpl_array.push([key, value]);
    }
    for (const [key, value] of Object.entries(bph_vals)) {
        bph_array.push([key, value]);
    }
    for (const [key, value] of Object.entries(bt_vals)) {
        bt_array.push([key, value]);
    }
    for (const [key, value] of Object.entries(sl_vals)) {
        sl_array.push([key, value]);
    }
    for (const [key, value] of Object.entries(ol_vals)) {
        ol_array.push([key, value]);
    }
    for (const [key, value] of Object.entries(hr_vals)) {
        hr_array.push([key, value]);
    }

    var options = {
        title: 'Patients by Diastolic Blood Pressure',
        height: 550,
        width: 550
    };

    var chart = new google.visualization.PieChart(document.getElementById('bpl_chart'));
    chart.draw(google.visualization.arrayToDataTable(bpl_array), options);

    var chart1 = new google.visualization.PieChart(document.getElementById('bph_chart'));
    options['title']='Patients by Systolic Blood Pressure'
    chart1.draw(google.visualization.arrayToDataTable(bph_array), options);

    var chart2 = new google.visualization.PieChart(document.getElementById('bt_chart'));
    options['title']='Patients by Body Temperature'
    chart2.draw(google.visualization.arrayToDataTable(bt_array), options);

    var chart3 = new google.visualization.PieChart(document.getElementById('sl_chart'));
    options['title']='Patients by Sugar Level'
    chart3.draw(google.visualization.arrayToDataTable(sl_array), options);

    var chart4 = new google.visualization.PieChart(document.getElementById('ol_chart'));
    options['title']='Patients by Oxygen Level'
    chart4.draw(google.visualization.arrayToDataTable(ol_array), options);

    var chart5 = new google.visualization.PieChart(document.getElementById('hr_chart'));
    options['title']='Patients by Heart Rate'
    chart5.draw(google.visualization.arrayToDataTable(hr_array), options);

}

function goToPatient(){
    localStorage.setItem('currentPatient', localStorage.getItem("mPatient"));
    window.location.reload();
}