var init = function init() {
    send_message_handler();
    messsage_preview_handler();
};

function reset_form(form) {
    form.find('input:text, input:password, input:file, select, textarea').val('');
    form.find('input:radio, input:checkbox').removeAttr('checked').removeAttr('selected');
}

function send_message_handler() {
    $('#send-email').click(function (event) {
        event.preventDefault();

        compile_markdown();

        var $form = $('#contact'),
            url = $form.attr('action'),
            email = $form.find('#visitor-email').val(),
            content = $form.find("#visitor-message-preview").prop('outerHTML');

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

function messsage_preview_handler() {
    $('#preview-btn').click(function () {
        compile_markdown();
    })
}

$(document).ready(init);
$(document).on('page:load', init);