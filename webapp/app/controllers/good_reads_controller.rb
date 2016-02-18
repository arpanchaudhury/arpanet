class GoodReadsController < ApplicationController

  def index
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : '0'
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : '5'
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []

    conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups"

    api_response = conn.get do |req|
      req.params['pageStart'] = page_start
      req.params['pageLength'] = page_length
      req.params['topics'] = @selected_topics
    end

    @topics = fetch_topics(@selected_topics, 5)
    api_response = JSON.parse(api_response.body)
    blog_post_count = api_response['count']
    @blog_posts = api_response['writeUps']

    @pager = {:count => blog_post_count, :page_start => page_start, :page_length => page_length}
  end

  def show
    write_up_id = params[:id]

    conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups/#{write_up_id}"

    api_response = conn.get

    @writeup = JSON.parse(api_response.body)
  end

  def topics
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []
    render json: {:topics => fetch_topics(@selected_topics, 10000)}
  end

  private def fetch_topics(selected_topics, count)
    conn = Faraday.new Rails.configuration.x.api.url

    api_response = conn.get '/write-ups/topics'

    all_topics = JSON.parse(api_response.body)
    topics = all_topics - selected_topics
    topics.take(count)
  end
end
