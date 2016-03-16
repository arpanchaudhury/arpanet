var page_start = 0;

function init_search() {
    var search_form = $('#search-form');
    toggle_search_event_handler(search_form);
    user_search_event_handler(search_form);
}

function toggle_search_event_handler(search_form) {
    search_form.find('.dropdown-item').click(function () {
        var dropdown_item = $(this),
            dropdown_toggle = dropdown_item.closest('.dropdown').find('.dropdown-toggle');
        dropdown_toggle.text(dropdown_item.text());
        dropdown_toggle.attr('value', dropdown_item.attr('value'));
    })
}

function user_search_event_handler(search_form) {
    search_form.find('.search-query').keydown(function (event) {
        if (event.keyCode == 13) {
            search(search_form)
        }
    });
    search_form.find('.search-btn').click(function () {
        search(search_form)
    });
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
    var html_data = $.parseHTML(data),
        other_sections = $('section:not(.search-results)'),
        search_section = $('section.search-results');
    other_sections.addClass('hidden');
    search_section.empty();
    search_section.append(html_data);
    search_section.removeClass('hidden');

    var total_results = search_section.find('.results-container').data('total-results');
    if (total_results == search_section.find('.snippet').length) {
        remove_load_more_button();
        page_start = 0;
    }

    load_more_event_handler();
    photograph_click_event_handler();
    photograph_view_modal_close_event_handler();
    hide_spinner_on_searched_photograph_load_event_handler();
}

function load_more_event_handler() {
    $('.search-result-loader').click(function () {
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

function photograph_view_modal_close_event_handler() {
    var modal = $('#photograph-view-modal');
    modal.find('.close').click(function () {
        modal.find('.modal-content').addClass('hidden');
        modal.find('.loader').removeClass('hidden');
    })
}

function append_search_data(data) {
    var html_data = $.parseHTML(data),
        loaded_search_results = $(html_data).find('.snippet-group'),
        search_section = $('section.search-results');
    search_section.find('.results-container').append(loaded_search_results);

    var total_results = search_section.find('.results-container').data('total-results');
    if (total_results == search_section.find('.snippet').length) {
        remove_load_more_button();
        page_start = 0;
    }

    photograph_click_event_handler();
    hide_spinner_on_searched_photograph_load_event_handler();
}

function remove_load_more_button() {
    $('.search-result-loader').remove();
}

function photograph_click_event_handler() {
    $('.photograph').click(function () {
        var image_url = $(this).find('img').attr('src'),
            modal = $('#photograph-view-modal');
        modal.find('img').attr('src', remove_all_params(image_url));
        modal.modal('show');
    })
}

function hide_spinner_on_searched_photograph_load_event_handler() {
    $('#photograph-view-modal').find('img').load(function () {
        var modal_content = $(this).closest('.modal-content'),
            loader = modal_content.siblings('.loader');
        loader.addClass('hidden');
        modal_content.removeClass('hidden');
    })
}

