var init = function init() {
    activate_carousel(0);
    image_click_handler();
    initialize_drawer($('.drawer'));
    slide_show_key_controls();
    slide_show_additional_controls();
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

    play_button.click(function() {
        pause_button.removeClass('hidden');
        play_button.addClass('hidden');
        carousel.carousel('cycle');
    });

    pause_button.click(function() {
        play_button.removeClass('hidden');
        pause_button.addClass('hidden');
        carousel.carousel('pause');
    });
}

// drawer component - can be extracted into a separate js file

function initialize_drawer(drawer) {
    drawer.addClass('opening');
    drawer.find('.drawer-item:not(:last)').addClass('hidden');
    if(drawer.find('.drawer-item').size() == 1) drawer.find('.drawer-icon').addClass('fa-angle-double-up');
    else drawer.find('.drawer-icon').addClass('fa-angle-double-down');
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