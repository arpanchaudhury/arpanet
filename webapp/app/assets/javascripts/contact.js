$('#send-email').click(function (event) {
    event.preventDefault();

    var $form = $('#contact'),
        url = $form.attr('action'),
        email = $form.find('#visitor-email').val(),
        content = $form.find("#visitor-message-body").val();

    $.post(url, {user_email: email, email_content: content});
});