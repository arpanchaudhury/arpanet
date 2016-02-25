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
