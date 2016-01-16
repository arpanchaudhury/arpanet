module PhotographyHelper
  def get_photography_url(image_detail)
    "#{Rails.configuration.x.api.url}/images/photography/#{image_detail['_id']}"
  end

  def get_photography_title(image_detail)
    image_detail['title']
  end

  def get_photography_description(image_detail)
    image_detail['description']
  end
end