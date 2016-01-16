require 'rest_client'

class PhotographyController < ApplicationController
  def index
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : 0
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : 9

    @image_details = JSON.parse(
        RestClient::Request.execute(
            method: :get, url: "#{Rails.configuration.x.api.url}/images/photography",
            timeout: 10, headers:{params:{'page-start': page_start, 'page-length': page_length}}
        )
    )
  end
end
