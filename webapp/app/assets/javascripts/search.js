//= require good_reads
//= require pager

function initialize_search_toggle_dropdown() {
    $('.search-toggle').click(function () {
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
            search_url = $form.attr('action') + '/' + content,
            query = $form.find('#search-term').val().trim();

        if (query != '') {
            $.get(search_url, {query: query}).done(populate_search_data);
        }
    });
}

function populate_search_data(data) {
    var sections = $('section'),
        search_section = $('#search-results');

    sections.addClass('hidden');
    search_section.replaceWith(data);

    after_timout(100, function () {
        resize_videos();
        resize_slides();
        disable_pager_buttons();

        recompute_height($('#search-results'));
    });
}

function recompute_height(search_section) {
    var recomputed_height = search_section.children().height();
    search_section.height(recomputed_height);
}

function after_timout(delay, f) {
    setTimeout(f, delay);
}