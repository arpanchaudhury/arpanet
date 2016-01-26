module PhotographyHelper
  def get_photograph_url(image_detail)
    "#{Rails.configuration.x.api.url}/images/photography/#{image_detail['_id']}"
  end

  def get_photograph_title(image_detail)
    image_detail['title']
  end

  def get_photograph_description(image_detail)
    image_detail['description']
  end

  def get_photograph_tags(image_detail)
    image_detail['tags']
  end
end