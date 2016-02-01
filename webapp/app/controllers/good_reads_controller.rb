class GoodReadsController < ApplicationController
  def index
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []
    @topics = fetch_topics(@selected_topics, 5)
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
