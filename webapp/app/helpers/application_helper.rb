module ApplicationHelper
  def get_resume_url
    "#{Rails.configuration.x.api.url}/resume"
  end
end
