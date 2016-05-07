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

    conn = Faraday.new "#{Rails.configuration.x.api.url}/photography/references"
    @photography_references = JSON.parse(conn.get.body)

    @pager = {:count => response_body['count'].to_i, :page_start => page_start.to_i, :page_length => page_length.to_i}

    if @tags.empty?
      @page_title = 'Photographs'
      @page_metadata = "List of photographs. Page #{page_start.to_i + 1} of #{@pager[:count] / page_length.to_i + 1}."
    else
      @page_title = "Photographs tagged with #{@tags.join(', ')}"
      @page_metadata = "List of photographs. Page #{page_start.to_i + 1} of #{@pager[:count] / page_length.to_i + 1}. These photographs are tagged with #{@tags.join(', ')}"
    end
  end
end
