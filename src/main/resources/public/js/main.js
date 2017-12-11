$(document).ready(function () {
    // https://datatables.net/plug-ins/sorting/date-euro
    $.extend(jQuery.fn.dataTableExt.oSort, {
        "date-euro-pre": function (a) {
            var x;

            if ($.trim(a) !== '') {
                var frDatea = $.trim(a).split(' ');
                var frTimea = (undefined != frDatea[1]) ? frDatea[1].split(':') : [00, 00, 00];
                var frDatea2 = frDatea[0].split('.');
                x = (frDatea2[2] + frDatea2[1] + frDatea2[0] + frTimea[0] + frTimea[1] + ((undefined != frTimea[2]) ? frTimea[2] : 0)) * 1;
            } else {
                x = Infinity;
            }
            return x;
        },
        "date-euro-asc": function (a, b) {
            return a - b;
        },
        "date-euro-desc": function (a, b) {
            return b - a;
        }
    });

    // Current rentals with euro date sorting
    $('#rentals').DataTable({columnDefs: [{type: 'date-euro', targets: [3]}]});
    // Rental history with euro date sorting
    $('#rentals_history').DataTable({columnDefs: [{type: 'date-euro', targets: [3, 4]}]});
    $('#usrs').DataTable(); // Users table
    $('#devs').DataTable(); // Device table

// Manage symbol "-". Each barcode of the tablet is length of 5
    $('#devices').bind('input propertychange', function (event) {
        // Remove "-" from input
        var text = event.target.value.replace(new RegExp("-", "g"), "");
        // Set input to ""
        $(event.target).val("");

        // Re-add "-"
        var finalText = "";
        var i = 0;
        for (var token in text) {
            if (i !== 0 && i % 5 === 0) {
                finalText += "-";
            }
            finalText += text[token];
            i++;
        }
        // Set text to input
        $(event.target).val(finalText.toUpperCase());
        // Update tablet counter
        $('#charNum').text(event.target.value.split("-").length);
    });


    // Async check that user exists.
    $("#personInformation").bind("input propertychange", function () {
        $.ajax({
            url: window.location.href + "exists",
            type: "POST",
            //data: {pin: $("#personInformation").val(), _csrf: $('meta[name="_csrf"]').attr('content')},
            data: {pin: $("#personInformation").val()},
            success: function (data) {
                if (typeof(data) === "string") {
                    $("#usr").text(data !== "" ? "Tere, " + data + "! ✔" : "Sisesta printeri kood ✘")
                        .removeClass("valid invalid").addClass(data !== "" ? "valid" : "invalid");
                }
            }
        });
    });

    // Add autofocus on modal elements
    $('.modal').on('shown.bs.modal', function () {
        $(this).find('[autofocus]').focus();
    });

    // Load the Visualization API and the corechart package.
    google.charts.load('current', {'packages': ['corechart', 'line', 'calendar']});

    var tabletHistory = [];

    function callDeviceUsagePieChart() {
        $.ajax({
            url: window.location.href + "_chart_device", type: 'GET', cache: false,
            success: function (data) {
                data.forEach(function (entry) {
                    tabletHistory.push([entry[0], parseInt(entry[1])]);
                });
                google.charts.setOnLoadCallback(pieChartDeviceUsage);
            }
        });
    }

    function pieChartDeviceUsage() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Seade');
        data.addColumn('number', 'Kasutusi');
        data.addRows(tabletHistory);
        var options = {
            title: "Aktiivsete seadmete kasutuste arv kokku",
            width: 500,
            height: 500
        };
        new google.visualization.PieChart(document.getElementById("piechart_device")).draw(data, options);
    }


    var userHistory = [];

    function callUserUsagePieChart() {
        $.ajax({
            url: window.location.href + "_chart_user", type: 'GET', cache: false,
            success: function (data) {
                data.forEach(function (entry) {
                    userHistory.push([entry[0], parseInt(entry[1])]);
                });
                google.charts.setOnLoadCallback(pieChartUserUsage);
            }
        });
    }

    function pieChartUserUsage() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'PIN');
        data.addColumn('number', 'Kasutusi');
        data.addRows(userHistory);
        var options = {
            title: "Aktiveeritud kasutajate aktiivsus laenutuste arvu lõikes",
            width: 500,
            height: 500
        };
        new google.visualization.PieChart(document.getElementById("piechart_user")).draw(data, options);
    }


    var monthHistory = [];
    var months = ["jaanuar", "veebruar", "märts", "aprill", "mai", "juuni", "juuli", "august", "september", "oktoober", "november", "detsember"];

    function callLineUsageMonthly() {
        $.ajax({
            url: window.location.href + "_chart_month", type: 'GET', cache: false,
            success: function (data) {
                data.forEach(function (entry) {
                    monthHistory.push([months[entry[0] - 1], entry[1]]);
                });
                google.charts.setOnLoadCallback(lineUsageMonthly);
            }
        });
    }


    function lineUsageMonthly() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Päev');
        data.addColumn('number', 'Kasutusi');
        data.addRows(monthHistory);
        var options = {
            title: "Seadmete kasutus käesoleva aasta lõikes",
            height: 500,
            pointsVisible: true,
            vAxis: {title: "Laenutusi"},
            hAxis: {title: "Kuu"}
        };
        new google.charts.Line(document.getElementById("line_month")).draw(data, google.charts.Line.convertOptions(options));
    }


    var hist = [];

    function callUsageHist() {
        $.ajax({
            url: window.location.href + "_chart_day", type: 'GET', cache: false,
            success: function (data) {
                data.forEach(function (entry) {
                    hist.push([entry]);
                });
                google.charts.setOnLoadCallback(usageHist);
            }
        });
    }


    function usageHist() {
        var data = new google.visualization.DataTable();
        data.addColumn('number', 'Seadmeid');
        data.addRows(hist);
        var options = {
            title: "Laenutavate seadmete kogus käesoleval aastal",
            height: 500,
            vAxis: {title: "Laenutuste arv"},
            hAxis: {title: "Seadmete arv"},
            histogram: {bucketSize: 1}
        };
        new google.visualization.Histogram(document.getElementById("hist_day")).draw(data, options);
    }

    var overall = [];

    function callOverall() {
        $.ajax({
            url: window.location.href + "_chart_overall", type: 'GET', cache: false,
            success: function (data) {
                data.forEach(function (entry) {
                    overall.push([new Date(entry[2], entry[1] - 1, entry[0]), entry[3]]);
                });
                google.charts.setOnLoadCallback(overallChart);
            }
        });
    }

    function overallChart() {
        var data = new google.visualization.DataTable();
        data.addColumn({type: 'date'});
        data.addColumn({type: 'number'});
        data.addRows(overall);
        var options = {
            title: "Laenutusi päevade lõikes",
            calendar: {
                dayOfWeekLabel: {
                    fontName: 'Times-Roman',
                    fontSize: 12,
                    cellSize: 16,
                    color: '#1a8763',
                    bold: true,
                    italic: true
                },
                dayOfWeekRightSpace: 10,
                daysOfWeek: 'PETKNRL'
            }
        };
        new google.visualization.Calendar(document.getElementById("overall")).draw(data, options);
    }


    if ($("#piechart_device").length) {
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(callDeviceUsagePieChart);
    }

    if ($("#piechart_user").length) {
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(callUserUsagePieChart);
    }

    if ($("#line_month").length) {
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(callLineUsageMonthly);
    }

    if ($("#hist_day").length) {
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(callUsageHist);
    }
    if ($("#overall").length) {
        // Set a callback to run when the Google Visualization API is loaded.
        google.charts.setOnLoadCallback(callOverall);
    }

    function resizeChart() {
        if ($("#line_month").length) lineUsageMonthly();
        if ($("#hist_day").length) usageHist();
        if ($("#overall").length) overallChart();
    }

    if (document.addEventListener) {
        window.addEventListener('resize', resizeChart);
    }
    else if (document.attachEvent) {
        window.attachEvent('onresize', resizeChart);
    }
    else {
        window.resize = resizeChart;
    }
});