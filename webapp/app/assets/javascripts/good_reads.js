function init() {
    initialize_topics_search_button();
    initialize_filters();
    initialize_autocomplete();
    autocomplete_select();
}

function initialize_topics_search_button() {
    $('.autosuggest-search-btn').click(function () {
        var input_box = $('.autosuggest-search-text');
        var topic = $(input_box).val().trim();
        location.href = add_parameter_to_URL('topics[]=' + topic);
    })
}

function initialize_filters() {
    $('.topic input').click(function () {
        var check_box = $(this);
        var topic = $(this).val();
        if (check_box.is(':checked')) location.href = add_parameter_to_URL('topics[]=' + topic);
        else location.href = remove_parameter_from_URL('topics[]=' + encodeURIComponent(topic));
    })
}

function initialize_autocomplete() {
    var _url = location.href;
    var url_parts = _url.split('?');
    var params = (url_parts.length > 1) ? '?' + url_parts[1] : '';
    $.get('/topics' + params, function (data) {
        $('.topic-search input').autocomplete({
            delay: 1000,
            minLength: 3,
            source: function (request, response) {
                var results = $.ui.autocomplete.filter(data.topics, request.term);
                response(results.slice(0, 3));
            }
        });
    });
}

function autocomplete_select() {
    $(".topic-search input").on("autocompleteselect", function (event, ui) {
        location.href = add_parameter_to_URL('topics[]=' + ui.item.value);
    });
}

$(document).ready(init);
$(document).on('page:load', init);