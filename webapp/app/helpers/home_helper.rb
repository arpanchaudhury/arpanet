module HomeHelper
  def get_profile_image_url(profile)
    "#{Rails.configuration.x.api.url}/images/public-image/#{profile['image_id']}"
  end

  def get_profile_summary(profile)
    profile['summary']
    end

  def get_profile_description(profile)
    profile['about']
  end
end