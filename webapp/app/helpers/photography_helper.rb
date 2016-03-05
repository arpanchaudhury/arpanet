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

  def get_equipment_title(equipment)
    equipment['title']
  end

  def get_equipment_image_url(equipment)
    equipment['img_src']
  end

  def get_equipment_description(equipment)
    equipment['description']
  end

  def get_equipment_specification_url(equipment)
    equipment['spec_url']
  end
end