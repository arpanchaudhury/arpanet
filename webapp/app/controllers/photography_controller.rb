class PhotographyController < ApplicationController
  def index
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : '0'
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : '9'
    @tags = request.query_parameters['tags'] ? request.query_parameters['tags'] : []

    conn = Faraday.new "#{Rails.configuration.x.api.url}/images/photography"
    api_response = conn.get do |req|
      req.params['pageStart'] = page_start
      req.params['pageLength'] = page_length
      req.params['tags'] = @tags
    end
    response_body = JSON.parse(api_response.body)
    @image_details = response_body['photographs']

    conn = Faraday.new "#{Rails.configuration.x.api.url}/photography/equipments"
    @photography_equipments = JSON.parse(conn.get.body)

    @pager = {:count => response_body['count'], :page_start => page_start, :page_length => page_length}
  end
end
