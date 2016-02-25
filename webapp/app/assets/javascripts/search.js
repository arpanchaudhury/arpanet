var page_start = 0;

function init() {
    var search_form = $('#search-form');
    toggle_search_event_handler(search_form);
    user_event_search_handler(search_form);
}

function toggle_search_event_handler(search_form) {
    search_form.find('.dropdown-item').click(function () {
        var dropdown_item = $(this),
            dropdown_toggle = dropdown_item.closest('.dropdown').find('.dropdown-toggle');
        dropdown_toggle.text(dropdown_item.text());
        dropdown_toggle.attr('value', dropdown_item.attr('value'));
    })
}

function user_event_search_handler(search_form) {
    search_form.find('.search-query').keydown(function (event) {
        if (event.keyCode == 13) {
            search(search_form)
        }
    });
    search_form.find('.search-btn').click(search(search_form));
}

function load_more_event_handler() {
    $('.loader').click(function () {
        page_start += 1;
        var search_form = $('#search-form'),
            dropdown = search_form.find('.dropdown-toggle'),
            search_url = search_form.attr('action') + '/' + dropdown.attr('value'),
            search_term = search_form.find('.search-query').val().trim();

        if (search_term != '') {
            $.get(search_url, {query: search_term, 'page-start': page_start}).done(append_search_data);
        }
    })
}

function photograph_click_event_handler() {
    $('.photograph').click(function () {
        var image_url = $(this).find('img').attr('src'),
            modal = $('#photograph-view-modal');
        modal.find('img').attr('src', image_url);
        modal.modal('show');
    })
}

function search(search_form) {
    var content_to_search = search_form.find('.dropdown .dropdown-toggle').attr('value'),
        search_url = search_form.attr('action') + '/' + content_to_search,
        query = search_form.find('.search-query').val().trim();

    if (query != '') {
        $.get(search_url, {query: query}).done(populate_initial_search_results);
    }
}

function populate_initial_search_results(data) {
    var other_sections = $('section:not(.search-results)'),
        search_section = $('section.search-results');
    other_sections.addClass('hidden');
    search_section.empty();
    search_section.append(data);
    search_section.removeClass('hidden');

    var total_results = search_section.find('.results-container').data('total-results');
    if (total_results == search_section.find('.snippet').length) {
        remove_load_more_button();
        page_start = 0;
    }

    load_more_event_handler();
    photograph_click_event_handler();
}

function append_search_data(data) {
    var html_data = $.parseHTML(data),
        loaded_search_results = $(html_data).find('.snippet'),
        search_section = $('section.search-results');
    search_section.find('.results-container').append(loaded_search_results);

    var total_results = search_section.find('.results-container').data('total-results');
    if (total_results == search_section.find('.snippet').length) {
        remove_load_more_button();
        page_start = 0;
    }

    photograph_click_event_handler();
}

function remove_load_more_button() {
    $('.loader').remove();
}

$(document).ready(init);
$(document).on('page:load', init);