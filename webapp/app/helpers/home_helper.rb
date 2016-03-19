module HomeHelper
  def get_profile_image_url(profile)
    "#{Rails.configuration.x.api.url}/images/public-image/#{profile['image_id']}"
  end

  def get_resized_profile_image_url(profile, max_width, max_height)
    "#{Rails.configuration.x.api.url}/images/public-image/#{profile['image_id']}?maxWidth=#{max_width}&maxHeight=#{max_height}"
  end

  def get_profile_summary(profile)
    profile['summary']
    end

  def get_profile_description(profile)
    profile['about']
  end

  def get_social_medias(profile)
    profile['social_medias']
  end
end