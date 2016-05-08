module PhotographyHelper
  def get_resized_hero_image_url(max_width, max_height)
    "#{Rails.configuration.x.api.url}/images/photography/hero-image?maxWidth=#{max_width}&maxHeight=#{max_height}"
  end

  def get_photograph_url(image_detail)
    "#{Rails.configuration.x.api.url}/images/photography/#{image_detail['_id']}"
  end

  def get_resized_photograph_url(image_detail, max_width, max_height)
    "#{Rails.configuration.x.api.url}/images/photography/#{image_detail['_id']}?maxWidth=#{max_width}&maxHeight=#{max_height}"
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

  def get_equipment_title(equipment)
    equipment['title']
  end

  def get_equipment_description(equipment)
    equipment['description']
  end

  def get_equipment_specification_url(equipment)
    equipment['spec_url']
  end

  def get_reference_title(reference)
    reference['title']
  end

  def get_reference_image_url(reference, max_width, max_height)
    "#{Rails.configuration.x.api.url}/images/public-image/#{reference['image_src']}?maxWidth=#{max_width}&maxHeight=#{max_height}"
  end

  def get_reference_description(reference)
    reference['description']
  end
end