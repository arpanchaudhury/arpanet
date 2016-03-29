function init_homepage() {
    profile_image_click_event_handler();
    hide_spinner_on_profile_picture_load_event_handler();
}

function profile_image_click_event_handler() {
    $('.profile-picture').click(function() {
        var image_url = $(this).attr('src'),
            modal = $('#profile-image-modal');
        modal.find('img').attr('src', remove_all_params(image_url));
        modal.modal('show');
    })
}

function hide_spinner_on_profile_picture_load_event_handler() {
    $('#profile-image-modal').find('img').load(function() {
        var modal_content = $(this).closest('.modal-content'),
            loader = modal_content.siblings('.loader');
        loader.addClass('hidden');
        modal_content.removeClass('hidden');
    })
}
