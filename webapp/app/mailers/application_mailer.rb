class ApplicationMailer < ActionMailer::Base
  default from: Rails.configuration.x.mailer.email
  layout 'mailer'
end
