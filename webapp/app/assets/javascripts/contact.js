var init = function init() {
    contact_form_init();
};

function reset_form(form) {
    form.find('input:text, input:password, input:file, select, textarea').val('');
    form.find('input:radio, input:checkbox').removeAttr('checked').removeAttr('selected');
}

function contact_form_init() {
    $('#send-email').click(function (event) {
        event.preventDefault();

        var $form = $('#contact'),
            url = $form.attr('action'),
            email = $form.find('#visitor-email').val(),
            content = $form.find("#visitor-message-body").val();

        if (email.trim() != '' && is_email(email) && content.trim() != '')
            $.post(url, {user_email: email, email_content: content});
        else {
            $form.find('.validation-text').text("E-mail should be valid and Message can't be blank");
            return;
        }
        reset_form($form);
        $form.closest('.modal').modal('hide');
    });
}

$(document).ready(init);