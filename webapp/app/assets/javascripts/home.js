function init_homepage() {
    profile_image_click_event_handler();
}

function profile_image_click_event_handler() {
    $('.profile-picture').click(function() {
        var image_url = $(this).attr('src'),
            modal = $('#profile-image-modal');
        modal.find('img').attr('src', remove_all_params(image_url));
        modal.modal('show');
    })
}