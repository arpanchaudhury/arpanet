//= require main
//= require modernizr

function init_good_reads() {
    initiate_timeline();
    disable_unused_pagination_buttons();
    initialize_autocomplete();
    topic_search_event_handler();
    topic_selection_event_handler();
    suggestion_selection_event_handler();
    resize_videos();
    resize_slides();
    window_resize_handler();
    compile_markdown($('#write-up'));
    code_copy_event_handler();
}

function initialize_autocomplete() {
    var topic_search_box = $('.topic-search'),
        url_parts = location.href.split('?'),
        params = (url_parts.length > 1) ? '?' + url_parts[1] : '';
    $.get('/topics' + params, function (data) {
        topic_search_box.find('.search-query').autocomplete({
            delay: 500,
            minLength: 3,
            source: function (request, response) {
                var results = $.ui.autocomplete.filter(data.topics, request.term);
                response(results.slice(0, 3));
            }
        });
    });
}

function topic_search_event_handler() {
    var topic_search_box = $('.topic-search');
    topic_search_box.find('.search-btn').click(function () {
        var url = location.href,
            topic = topic_search_box.find('.search-query').val().trim(),
            page_start_param = /page-start=\d+/.exec(url),
            sanitized_url = remove_parameter_from_URL(url, page_start_param);
        location.href = add_parameter_to_URL(sanitized_url, 'topics[]=' + topic);
    })
}

function topic_selection_event_handler() {
    var topic_filter_box = $('.topic-filter');
    topic_filter_box.find('.topic').click(function () {
        var url = location.href,
            topic = $(this).attr('value'),
            page_start_param = /page-start=\d+/.exec(url),
            sanitized_url = remove_parameter_from_URL(url, page_start_param);
        if ($(this).find('input').is(':checked')) location.href = add_parameter_to_URL(sanitized_url, 'topics[]=' + topic);
        else location.href = remove_parameter_from_URL(sanitized_url, 'topics[]=' + encodeURIComponent(topic));
    })
}

function suggestion_selection_event_handler() {
    var topic_search_box = $('.topic-search');
    topic_search_box.find('.search-query').on("autocompleteselect", function (event, ui) {
        var url = location.href,
            page_start_param = /page-start=\d+/.exec(url),
            sanitized_url = remove_parameter_from_URL(url, page_start_param);
        location.href = add_parameter_to_URL(sanitized_url, 'topics[]=' + ui.item.value);
    });
}

function resize_videos() {
    var videos = $('.video');
    var video_container = $('.video-container').first();
    var calculated_container_width = video_container.width();
    var calculated_container_height = calculated_container_width * 9.0 / 16.0;
    videos.attr('width', calculated_container_width);
    videos.attr('height', calculated_container_height);
}

function resize_slides() {
    var slides = $('.slide');
    var slide_container = $('.slide-container').first();
    var calculated_container_width = slide_container.width();
    var calculated_container_height = calculated_container_width * 9.0 / 16.0;
    slides.attr('width', calculated_container_width);
    slides.attr('height', calculated_container_height);
}

function window_resize_handler() {
    $(window).resize(function () {
        resize_videos();
        resize_slides();
    })
}

function code_copy_event_handler() {
    $('.code').find('.copy').click(function () {
        var to_copy = document.querySelector('#repo-url');
        to_copy.select();
        try {
            document.execCommand('copy');
        } catch (err) {}
    })
}