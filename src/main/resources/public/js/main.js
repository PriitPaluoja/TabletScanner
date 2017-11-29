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
        $(event.target).val(finalText);
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
                    $("#usr").text(data !== "" ? "Tere, " + data + "! ✔" : "Sisesta laenutaja/tagastaja PIN ✘")
                        .removeClass("valid invalid").addClass(data !== "" ? "valid" : "invalid");
                }
            }
        });
    });

    // Add autofocus on modal elements
    $('.modal').on('shown.bs.modal', function () {
        $(this).find('[autofocus]').focus();
    });
});