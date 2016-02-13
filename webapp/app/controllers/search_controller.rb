class SearchController < ApplicationController
  def search
    content = params[:content]
    query = request.query_parameters['query']
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : '0'
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : '10'

    conn = Faraday.new "#{Rails.configuration.x.api.url}/search/#{content}"

    api_response = conn.get do |req|
      req.params['searchTerm'] = query
      req.params['pageStart']  = page_start
      req.params['pageLength'] = page_length
    end

    search_results = api_response.body

    render json: {:results => search_results}
  end
end