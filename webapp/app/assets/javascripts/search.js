function initialize_search_toggle_dropdown() {
    $('.search-toggle').click( function () {
        var toggle_button = $(this),
            dropdown_button = $('#search-dropdown');
        dropdown_button.text(toggle_button.text());
        dropdown_button.attr('value', toggle_button.attr('value'));
    })
}

function user_search_handler() {
    $('#search').click(function (event) {
        event.preventDefault();

        var $form = $('#search-form'),
            dropdown = $('#search-dropdown'),
            content = dropdown.attr('value'),
            url = $form.attr('action') + '/' + content,
            query = $form.find('#search-term').val().trim();

        if (query != '') {
            $.get(url, {'query' : query}).done(populate_search_data);
        }
    });
}

function populate_search_data(data) {
    var sections = $('section'),
        search_section = $('section#search-section');
    search_section.empty();
    search_section.append("<h1>" + JSON.stringify(data.results) + "</h1>");
    sections.addClass('hidden');
    search_section.removeClass('hidden');
}