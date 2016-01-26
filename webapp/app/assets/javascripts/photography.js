var init = function init() {
    activate_carousel(0);
    image_click_handler();
    slide_show_key_controls();
    slide_show_additional_controls();
    populate_tag_links();
    initiate_tag_removal_links();
    initialize_drawer($('.drawer'));
};

function image_click_handler() {
    $('.photo-small').click(function (event) {
        event.preventDefault();
        var target_carousel_item = $(this).attr("target-carousel-item");
        $('.carousel').carousel(parseInt(target_carousel_item));
    })
}

function activate_carousel(i) {
    var carouselItems = $('.carousel-inner').children();
    var carouselIndicators = $('.carousel-indicators li');
    $(carouselItems[i]).addClass('active');
    $(carouselIndicators[i]).addClass('active');
}

function slide_show_key_controls() {
    $("body").keydown(function (event) {
        var carousel = $('.carousel');
        if (event.keyCode == 37) {
            carousel.carousel('prev')
        }
        else if (event.keyCode == 39) {
            carousel.carousel('next')
        }
    });
}

function slide_show_additional_controls() {
    var carousel = $('.carousel');
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

function populate_tag_links() {
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

function initiate_tag_removal_links() {
    $('.remove-tag').each(function () {
        var tag = $(this);
        var url = location.href;
        var param = "tags[]=" + tag.text().trim();
        if (url.indexOf(param) > 0) {
            var _url = remove_parameter_from_URL(param);
            tag.attr('href', _url);
        }
    })
}

function add_parameter_to_URL(param) {
    var _url = location.href;
    _url += (_url.split('?')[1] ? '&' : '?') + param;
    return _url;
}

function remove_parameter_from_URL(param) {
    var _url = location.href;
    var url_parts = _url.split('?');
    if (url_parts.length == 1) return _url;
    else {
        var query_params = url_parts[1].split(/[&]/g);
        var updated_query_params = $.grep(query_params, function (query_param) {
            return query_param != param;
        });
        if (updated_query_params.length == 0) return url_parts[0];
        else return url_parts[0] + '?' + updated_query_params.join('&');
    }
}

// drawer component - can be extracted into a separate js file

function initialize_drawer(drawer) {
    drawer.find('.drawer-item:not(:last)').addClass('hidden');

    var areItemsHidden = drawer.find(".drawer-item.hidden").length == drawer.find(".drawer-item").length;
    var areItemsDisplayed = drawer.find(".drawer-item").not('.hidden').length == drawer.find(".drawer-item").length;

    if (areItemsHidden) {
        drawer.addClass('opening');
        drawer.find('.drawer-icon').addClass('fa-angle-double-down');
    }
    else if (areItemsDisplayed) {
        drawer.addClass('closing');
        drawer.find('.drawer-icon').addClass('fa-angle-double-up');
    }
    else {
        drawer.addClass('opening');
        drawer.find('.drawer-icon').addClass('fa-angle-double-down');
    }

    initialize_drawer_interactions(drawer);
}

function initialize_drawer_interactions(drawer) {
    drawer.find('.drawer-button').click(function (event) {
        event.preventDefault();

        var drawer = $(this).closest('.drawer');
        if (drawer.hasClass('opening')) show_next_item(drawer);
        else if (drawer.hasClass('closing')) hide_previous_item(drawer);

        var areItemsHidden = drawer.find(".drawer-item.hidden").length == drawer.find(".drawer-item").length;
        var areItemsDisplayed = drawer.find(".drawer-item").not('.hidden').length == drawer.find(".drawer-item").length;
        if (areItemsHidden || areItemsDisplayed) {
            toggle_drawer_icon(drawer);
            toggle_drawer_behavior(drawer);
        }
    })
}

function toggle_drawer_icon(drawer) {
    if ($(drawer).hasClass('opening')) drawer.find('.drawer-icon').removeClass('fa-angle-double-down').addClass('fa-angle-double-up');
    else if ($(drawer).hasClass('closing')) drawer.find('.drawer-icon').removeClass('fa-angle-double-up').addClass('fa-angle-double-down');
}

function toggle_drawer_behavior(drawer) {
    if (drawer.hasClass('opening')) drawer.removeClass('opening').addClass('closing');
    else if (drawer.hasClass('closing')) drawer.removeClass('closing').addClass('opening');
}

function hide_previous_item(drawer) {
    var displayItems = drawer.find('.drawer-item').not('.hidden');
    var itemToHide = $(displayItems).first();
    itemToHide.addClass('hidden');
}

function show_next_item(drawer) {
    var hiddenItems = $(drawer).find('.drawer-item.hidden');
    var itemToShow = $(hiddenItems).last();
    itemToShow.removeClass('hidden');
}

$(document).ready(init);
$(document).on('page:load', init);