//= require good_reads

var total_results = 0;
var page_start = 0;

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

    var updated_search_section = $('#search-results');
    total_results = updated_search_section.data('total-results');

    if(total_results == updated_search_section.find('.snippet').length) {
        remove_load_more_button();
        page_start = 0;
    }

    handle_photograph_snippet_click();
    load_more_button_handler();

    after_timout(500, function () {
        recompute_section_height(updated_search_section);
    });
}

function append_search_data(data) {
    var pruned_data = $($.parseHTML(data)).find('.snippet');

    $('.results-container').append(pruned_data);

    var updated_search_section = $('#search-results');
    total_results = updated_search_section.data('total-results');

    if(total_results == updated_search_section.find('.snippet').length) {
        remove_load_more_button();
        page_start = 0;
    }

    after_timout(500, function () {
        recompute_section_height(updated_search_section);
    });
}

function load_more_button_handler() {
    $('.loader:not(.disabled)').click(function() {
        page_start += 1;

        var $form = $('#search-form'),
            dropdown = $('#search-dropdown'),
            content = dropdown.attr('value'),
            search_url = $form.attr('action') + '/' + content,
            query = $form.find('#search-term').val().trim();

        if (query != '') {
            $.get(search_url, {query: query, 'page-start': page_start}).done(append_search_data);
        }
    })
}

function remove_load_more_button() {
    $('.loader').remove();
}

function recompute_section_height(search_section) {
    var recomputed_height = search_section.children().height() + 100;
    search_section.height(recomputed_height);
}

function after_timout(delay, f) {
    setTimeout(f, delay);
}

function handle_photograph_snippet_click() {
    $('.photograph').click(function() {
        var image_url = $(this).find('img').attr('src'),
            modal = $('#photograph-view-modal');
        modal.find('img').attr('src', image_url);
        modal.modal('show');
    })
}