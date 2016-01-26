class PhotographyController < ApplicationController
  def index
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : 0
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : 9
    tags = request.query_parameters['tags'] ? request.query_parameters['tags'] : []

    conn = Faraday.new "#{Rails.configuration.x.api.url}/images/photography"

    api_response = conn.get do |req|
      req.params['pageStart'] = page_start
      req.params['pageLength'] = page_length
      req.params['tags'] = tags
    end

    @image_details = JSON.parse(api_response.body)
  end
end
