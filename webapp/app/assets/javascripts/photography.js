function init_photography() {
    initialize_carousel();
    hide_spinner_on_photographs_load_event_handler();
    generate_tag_links();
    generate_tag_removal_links();
    initialize_drawer($('.drawer'));
    remove_unused_pagination_buttons();
}

function initialize_carousel() {
    var carousel = $('#carousel');
    carousel.carousel('pause');
    activate_carousel_from(carousel, 0);
    carousel_key_controls_event_handler(carousel);
    carousel_additional_controls_event_handler(carousel);
    image_click_event_handler(carousel);
}

function activate_carousel_from(carousel, index) {
    var carouselItems = carousel.find('.carousel-item');
    var carouselIndicators = carousel.find('.carousel-indicators li');
    $(carouselItems[index]).addClass('active');
    $(carouselIndicators[index]).addClass('active');
}

function carousel_key_controls_event_handler(carousel) {
    $("body").keydown(function (event) {
        if (event.keyCode == 37) {
            carousel.carousel('prev')
        }
        else if (event.keyCode == 39) {
            carousel.carousel('next')
        }
    });
}

function carousel_additional_controls_event_handler(carousel) {
    var carousel_additional_controls = carousel.find('.carousel-additional-controls');
    var play_button = carousel_additional_controls.find('.play-btn');
    var pause_button = carousel_additional_controls.find('.pause-btn');

    play_button.click(function () {
        pause_button.removeClass('hidden');
        play_button.addClass('hidden');
        carousel.carousel('cycle');
    });

    pause_button.click(function () {
        play_button.removeClass('hidden');
        pause_button.addClass('hidden');
        carousel.carousel('pause');
    });
}

function image_click_event_handler(carousel) {
    $('.photo-small').click(function () {
        var target_carousel_item = $(this).attr("target-carousel-item");
        carousel.carousel(parseInt(target_carousel_item));
    });
}

function generate_tag_links() {
    $('.tag').each(function () {
        var tag = $(this),
            url = location.href,
            page_start_param = /page-start=\d+/.exec(url),
            sanitized_url = remove_parameter_from_URL(url, page_start_param),
            param = "tags[]=" + tag.text();
        if (sanitized_url.indexOf(param) < 0) {
            var _url = add_parameter_to_URL(sanitized_url, param);
            tag.attr('href', _url);
        }
    })
}

function generate_tag_removal_links() {
    $('.remove-tag').each(function () {
        var tag = $(this),
            url = location.href,
            page_start_param = /page-start=\d+/.exec(url),
            sanitized_url = remove_parameter_from_URL(url, page_start_param),
            param = "tags[]=" + encodeURIComponent(tag.text().trim());
        if (sanitized_url.indexOf(param) > 0) {
            var _url = remove_parameter_from_URL(sanitized_url, param);
            tag.attr('href', _url);
        }
    })
}

function hide_spinner_on_photographs_load_event_handler() {
    $('#photograph-modal').find('img').load(function() {
        var photograph_modal = $("#photograph-modal"),
            modal_content = photograph_modal.find(".modal-content"),
            loader = modal_content.siblings('.loader');
        loader.addClass('hidden');
        modal_content.removeClass('hidden');
    })
}
