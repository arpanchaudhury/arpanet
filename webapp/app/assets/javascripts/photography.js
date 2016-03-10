function init_photography() {
    initialize_carousel();
    generate_tag_links();
    generate_tag_removal_links();
    initialize_drawer($('.drawer'));
    remove_unused_pagination_buttons();
}

function initialize_carousel() {
    var carousel = $('#carousel');
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
        var tag = $(this);
        var url = location.href;
        var param = "tags[]=" + tag.text();
        if (url.indexOf(param) < 0) {
            var _url = add_parameter_to_URL(param);
            tag.attr('href', _url);
        }
    })
}

function generate_tag_removal_links() {
    $('.remove-tag').each(function () {
        var tag = $(this);
        var url = location.href;
        var param = "tags[]=" + encodeURIComponent(tag.text().trim());
        if (url.indexOf(param) > 0) {
            var _url = remove_parameter_from_URL(param);
            tag.attr('href', _url);
        }
    })
}
