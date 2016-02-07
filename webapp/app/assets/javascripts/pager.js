function disable_pager_buttons() {
    var button_to_disable = $(".pager a[href='#']");
    button_to_disable.parent().addClass('disabled');
}