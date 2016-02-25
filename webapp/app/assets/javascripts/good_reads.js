//= require pager
//= require markdown
//= require url_helpers
//= require main
//= require modernizr

function init() {
    initiate_timeline();
    remove_unused_pagination_buttons();
    initialize_autocomplete();
    topic_search_event_handler();
    topic_selection_event_handler();
    suggestion_selection_event_handler();
    resize_videos();
    resize_slides();
    window_resize_handler();
    compile_markdown($('#write-up'));
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
        var topic = topic_search_box.find('.search-query').val().trim();
        location.href = add_parameter_to_URL('topics[]=' + topic);
    })
}

function topic_selection_event_handler() {
    var topic_filter_box = $('.topic-filter');
    topic_filter_box.find('.topic').click(function () {
        var topic = $(this).attr('value');
        if ($(this).find('input').is(':checked')) location.href = add_parameter_to_URL('topics[]=' + topic);
        else location.href = remove_parameter_from_URL('topics[]=' + encodeURIComponent(topic));
    })
}

function suggestion_selection_event_handler() {
    var topic_search_box = $('.topic-search');
    topic_search_box.find('.search-query').on("autocompleteselect", function (event, ui) {
        location.href = add_parameter_to_URL('topics[]=' + ui.item.value);
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

$(document).ready(init);
$(document).on('page:load', init);