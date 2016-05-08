//= require parallax

function init_photography() {
    generate_tag_links();
    generate_tag_removal_links();
    disable_unused_pagination_buttons();
    image_click_event_handler();
    hide_spinner_on_photograph_load_event_handler();
    initialize_carousel();
    hide_spinner_on_photographs_load_event_handler();
    initialize_drawer($('.drawer'));
    initialize_tooltip();
    initialize_parallax();
}

function initialize_parallax() {
    var parallax_window = $('.parallax-window');
    parallax_window.parallax({imageSrc: parallax_window.data('image-src')});
}

function initialize_tooltip() {
    $('[data-toggle="tooltip"]').tooltip();
}

function initialize_carousel() {
    var carousel = $('#carousel');
    activate_carousel_from(carousel, 0);
    carousel_key_controls_event_handler(carousel);
    carousel_additional_controls_event_handler(carousel);
    slide_show_button_click_event_handler(carousel);
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

function image_click_event_handler() {
    $('.photo-small').click(function () {
        var image_url = $(this).attr('src'),
            image_title = $(this).closest('.card').find('.card-title').text(),
            image_description = $(this).closest('.card').find('.card-text').text(),
            modal = $('#photograph-view-modal');
        modal.find('img').attr('src', remove_all_params(image_url));
        modal.find('.title').text(image_title);
        modal.find('.description').text(image_description);
        modal.modal('show');
    })
}

function hide_spinner_on_photograph_load_event_handler() {
    $('#photograph-view-modal').find('img').load(function () {
        var modal_content = $(this).closest('.modal-content'),
            loader = modal_content.siblings('.loader');
        loader.addClass('hidden');
        modal_content.removeClass('hidden');
    })
}

function slide_show_button_click_event_handler(carousel) {
    $('.slide-show').click(function () {
        carousel.carousel(0);
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
    var images_to_load = $('.cards .card').length, loaded_image_count = 0;

    $('#photograph-modal').find('img').load(function() {
        loaded_image_count += 1;
        if(loaded_image_count == images_to_load) {
            var photograph_modal = $("#photograph-modal"),
                modal_content = photograph_modal.find(".modal-content"),
                loader = modal_content.siblings('.loader');
            loader.addClass('hidden');
            modal_content.removeClass('hidden');
            loaded_image_count = 0;
        }
    })
}
