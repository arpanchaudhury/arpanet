module ApplicationHelper
  def get_resume_url
    "#{Rails.configuration.x.api.url}/resume"
  end

  def get_next_page_url(pager, path, other_params)
    page_start = pager[:page_start].to_i
    page_length = pager[:page_length].to_i
    pages = (pager[:count].to_f / page_length).ceil.to_i
    if page_start < pages - 1
      others = other_params.collect { |other_param_key, other_param_value|
        (other_param_value.collect { |value| "#{other_param_key}[]=#{value}" }).join('&')
      }.join('&')
      if others.empty?
        "#{path}?page-start=#{page_start + 1}&page-length=#{page_length}"
      else
        "#{path}?page-start=#{page_start + 1}&page-length=#{page_length}&#{others}"
      end
    else
      '#'
    end
  end

  def get_previous_page_url(pager, path, other_params)
    page_start = pager[:page_start].to_i
    page_length = pager[:page_length].to_i
    if page_start > 0
      others = other_params.collect { |other_param_key, other_param_value|
        (other_param_value.collect { |value| "#{other_param_key}[]=#{value}" }).join('&')
      }.join('&')
      if others.empty?
        "#{path}?page-start=#{page_start - 1}&page-length=#{page_length}"
      else
        "#{path}?page-start=#{page_start - 1}&page-length=#{page_length}&#{others}"
      end
    else
      '#'
    end
  end
end
