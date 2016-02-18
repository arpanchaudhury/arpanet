class SearchController < ApplicationController
  def search
    @content = params[:content]
    query = request.query_parameters['query']
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : '0'

    page_length_default = if @content == 'write-ups'
                            '10'
                          elsif @content == 'photographs'
                            '16'
                          end
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : page_length_default


    conn = Faraday.new "#{Rails.configuration.x.api.url}/search/#{@content}"

    api_response = conn.get do |req|
      req.params['searchTerm'] = query
      req.params['pageStart']  = page_start
      req.params['pageLength'] = page_length
    end

    response = JSON.parse(api_response.body)
    search_results_count = response['total']
    @search_results = response['results']
    @pager = {:count => search_results_count, :page_start => page_start, :page_length => page_length}

    render :layout => false
  end
end