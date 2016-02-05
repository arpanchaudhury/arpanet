class GoodReadsController < ApplicationController
  def index
    page_start = request.query_parameters['page-start'] ? request.query_parameters['page-start'] : 0
    page_length = request.query_parameters['page-length'] ? request.query_parameters['page-length'] : 10
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []

    conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups"

    api_response = conn.get do |req|
      req.params['pageStart'] = page_start
      req.params['pageLength'] = page_length
      req.params['topics'] = @selected_topics
    end

    @topics = fetch_topics(@selected_topics, 5)
    @blog_posts = JSON.parse(api_response.body)
  end

  def topics
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []
    render json: {:topics => fetch_topics(@selected_topics, 10000)}
  end

  private def fetch_topics(selected_topics, count)
    topics = ['first topic', 'second topic', 'third topic', 'fourth topic', 'fifth topic', 'sixth topic', 'seventh topic', 'eighth topic', 'ninth topic', 'tenth topic'] - selected_topics
    topics.take(count)
  end
end
