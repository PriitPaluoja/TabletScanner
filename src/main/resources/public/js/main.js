// For search box:
const getUrl = window.location;
const loc = getUrl.protocol + "//" + getUrl.host;


$(document).ready(function () {
    // Add tooltip
    $('[data-toggle="tooltip"]').tooltip();
    // Question filtering page: reset choices on button click
    $("#reset").click(function () { // Clear radiobox selection
        $('input:radio[id^=questions]:checked').prop('checked', false);
    });


    // In details and add page: covert assignable questions table to the Datatable
    $('#rentals').DataTable();
    $('#rentals_history').DataTable();


    // Search box autocomplete
    $("#query").autocomplete({
        source: loc + "/auto",
        delay: 50,
        select: function (event, ui) {
            $("input#query").val(ui.item.value);
            $('#sr').click(); // search user selected option
        }
    });

    // Results page results as datatable
    var table = $('#results').DataTable();



    // Make results page datatable rows expandable
    $('#results tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

})
;