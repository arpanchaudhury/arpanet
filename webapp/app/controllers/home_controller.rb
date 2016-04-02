class HomeController < ApplicationController
  def index
    conn = Faraday.new "#{Rails.configuration.x.api.url}/profile"
    api_response = conn.get
    @profile = JSON.parse(api_response.body)

    @page_title = 'Profile Summary'
    @page_metadata = 'A small summary of profile, career objectives, skill set, personal interests and hobbies. It includes social media profile links.'
  end

  def contact
    ContactMailer.personal_mail(params[:user_email], params[:email_content]).deliver_now
    render nothing: true
  end
end
