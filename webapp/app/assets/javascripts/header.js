//= require search

function init() {
    initialize_search_toggle_dropdown();
    user_search_handler();
}

$(document).ready(init);
$(document).on('page:load', init);