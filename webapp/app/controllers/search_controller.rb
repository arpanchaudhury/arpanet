class SearchController < ApplicationController
  def search
    @content = params[:content]
    @query = request.query_parameters['query']
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : '0'

    default_page_length =
      if @content == 'write-ups'
        '10'
      elsif @content == 'photographs'
        '16'
      end
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : default_page_length

    conn = Faraday.new "#{Rails.configuration.x.api.url}/search/#{@content}"

    api_response = conn.get do |req|
      req.params['searchTerm'] = @query
      req.params['pageStart']  = page_start
      req.params['pageLength'] = page_length
    end

    @search_results = JSON.parse(api_response.body)

    render :layout => false
  end
end