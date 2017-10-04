function setCharAt(str, index, chr) {
    if (index > str.length - 1) return str;
    return str.substr(0, index + 1) + chr + str.substr(index + 1);
}

$(document).ready(function () {
    // In details and add page: covert assignable questions table to the Datatable
    $('#rentals').DataTable();
    $('#rentals_history').DataTable();
    $('#usrs').DataTable();
    $('#devs').DataTable();


    var total_length = 0;
    $('#devices').bind('input propertychange', function (event) {
        var inputLength = event.target.value.length;

        if (inputLength > total_length && (inputLength % 5 === 0)) {
            var thisVal = event.target.value;
            var newValue = setCharAt(thisVal, thisVal.length - 2, '-');
            $(event.target).val(newValue);
            total_length = newValue.length;
        }

        total_length = inputLength;

        $('#charNum').text(event.target.value.split("-").length);
    })
});