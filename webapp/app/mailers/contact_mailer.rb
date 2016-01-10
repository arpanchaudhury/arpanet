class ContactMailer < ApplicationMailer
  def personal_mail(user_email, mail_content)
    @user_email = user_email
    @mail_content = mail_content
    mail(to: Rails.configuration.x.owner.email, subject: 'from visitor of your personal website')
  end
end
