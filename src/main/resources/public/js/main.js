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

    if ($('#main').length) {
        // Details page
        new Vue({
            el: "#main",
            data: {
                toDisable: true
            },
            methods: {
                makeEditable: function (event) {
                    this.toDisable = !this.toDisable;

                    if (this.toDisable) {
                        $('div[id^="question_new_add"]').remove();
                    }
                },
                deleteAttachment: function (event) {
                    $(event.currentTarget).parent().parent().remove();
                },
                deleteQuestion: function (event) {
                    $(event.currentTarget).parent().parent().remove();
                },
                getNewQuestionBox: function (event) {
                    if (!this.toDisable) {
                        getQuestionBox(event)
                    }
                },
                upvote: function (event) {
                    const alertBox = $("#msg");

                    alertBox.removeClass('in alert-success alert-danger');
                    $.get(loc + "/vote", {id: event.target.id.split("_")[1]}, function (data, status) {
                        if (status === "success") {
                            alertBox.addClass('alert-success in');
                            alertBox.text('Successfully up-voted!');
                        }
                        else {
                            alertBox.addClass('alert-danger in');
                            alertBox.text('Up-voting failed. Are you signed in?');
                        }
                    });
                }
            },
        })
        ;
    }
})
;