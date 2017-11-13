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


    $('#devices').bind('input propertychange', function (event) {
        var text = event.target.value.replace(new RegExp("-", "g"), "");
        $(event.target).val("");

        var finalText = "";
        var i = 0;
        for (var token in text) {
            if (i !== 0 && i % 5 === 0) {
                finalText += "-";
            }
            finalText += text[token];
            i++;
        }
        $(event.target).val(finalText);
        $('#charNum').text(event.target.value.split("-").length);
    })
});