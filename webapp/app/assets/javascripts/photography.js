var init = function init() {
    activate_carousel(0);
    image_click_handler();
};

function activate_carousel(i) {
    var carouselItems = $('.carousel-inner').children();
    var carouselIndicators = $('.carousel-indicators li');
    $(carouselItems[i]).addClass('active');
    $(carouselIndicators[i]).addClass('active');
}

function image_click_handler() {
    $('.photo-small').click(function (event) {
            event.preventDefault();

            var target_carousel_item = $(this).attr("target-carousel-item");
            $('.carousel').carousel(parseInt(target_carousel_item));
        }
    )
}

$(document).ready(init);
$(document).on('page:load', init);