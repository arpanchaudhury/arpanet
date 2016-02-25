function remove_unused_pagination_buttons() {
    var button_with_no_link = $(".pager a[href='']");
    button_with_no_link.parent().remove();
}