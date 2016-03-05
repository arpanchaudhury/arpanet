class GoodReadsController < ApplicationController

  def index
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : '0'
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : '10'
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []

    conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups"
    api_response = conn.get do |req|
      req.params['pageStart'] = page_start
      req.params['pageLength'] = page_length
      req.params['topics'] = @selected_topics
    end
    response_body = JSON.parse(api_response.body)
    @blog_posts = response_body['writeUps']

    @topics = fetch_topics(@selected_topics, 10)
    @pager = {:count => response_body['count'], :page_start => page_start, :page_length => page_length}
  end

  def show
    write_up_id = params[:id]
    conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups/#{write_up_id}"
    @blog_post = JSON.parse(conn.get.body)
  end

  def topics
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []
    render json: {:topics => fetch_topics(@selected_topics, 10000)}
  end

  private def fetch_topics(selected_topics, count)
    conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups/topics"
    all_topics = JSON.parse(conn.get.body)
    topics = all_topics - selected_topics
    topics.take(count)
  end
end
