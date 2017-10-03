$(document).ready(function () {
    // In details and add page: covert assignable questions table to the Datatable
    $('#rentals').DataTable();
    $('#rentals_history').DataTable();
    $('#usrs').DataTable();
    $('#devs').DataTable();


    $(document ).ready(function() {
        $('#devices').bind('keyup','keydown', function(event) {
            var inputLength = event.target.value.length;
            if (event.keyCode !== 8){
                if((inputLength + 1) % 6 === 0){
                    var thisVal = event.target.value;
                    thisVal += '-';
                    $(event.target).val(thisVal);
                }
            }
        })
    });
});