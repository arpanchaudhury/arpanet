module SearchHelper
  def get_popular_search_terms(content_type)
    if content_type == 'write-ups'
      conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups/topics"
      api_response = conn.get
      JSON.parse(api_response.body).take(10)
    else
      conn = Faraday.new "#{Rails.configuration.x.api.url}/images/photography/tags"
      api_response = conn.get
      JSON.parse(api_response.body).take(10)
    end
  end
end