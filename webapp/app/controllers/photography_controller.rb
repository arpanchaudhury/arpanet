class PhotographyController < ApplicationController
  def index
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : '0'
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : '6'
    tags = request.query_parameters['tags'] ? request.query_parameters['tags'] : []

    conn = Faraday.new "#{Rails.configuration.x.api.url}/images/photography"

    api_response = conn.get do |req|
      req.params['pageStart'] = page_start
      req.params['pageLength'] = page_length
      req.params['tags'] = tags
    end

    @tags = tags
    api_response = JSON.parse(api_response.body)
    image_count = api_response['count']
    @image_details = api_response['photographs']
    @drawer_items = [
        {
            :title => 'Drawer',
            :img_src => '/assets/nikkon-3300.jpg',
            :img_alt_txt => '...',
            :description => 'this is some text which is a filler to test the drawer behavior',
            :spec_url => '#'
        },
        {
            :title => 'Drawer',
            :img_src => '/assets/nikkon-3300.jpg',
            :img_alt_txt => '...',
            :description => 'this is some text which is a filler to test the drawer behavior',
            :spec_url => '#'
        }
    ]

    @pager = {:count => image_count, :page_start => page_start, :page_length => page_length}
  end
end
