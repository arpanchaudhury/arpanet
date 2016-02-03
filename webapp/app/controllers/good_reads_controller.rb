class GoodReadsController < ApplicationController
  def index
    @selected_topics = request.query_parameters['topics'] ? request.query_parameters['topics'].uniq : []
    @topics = fetch_topics(@selected_topics, 5)
    @blog_posts = [
      {
        type: 'text',
        title: 'Lorem ipsum dolor sit amet',
        content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur a interdum velit.
                  Quisque sit amet justo porta, varius mauris in, rutrum dui. Pellentesque eu est nec
                  ante convallis auctor. Fusce semper, justo a efficitur condimentum, orci erat cursus
                  diam, eu maximus neque velit in erat. Fusce at diam nisl. In feugiat luctus lacinia.
                  Nullam sagittis aliquet nibh id tempor. Aliquam erat volutpat. Pellentesque habitant
                  morbi tristique senectus et netus et malesuada fames ac turpis egestas.'
      },
      {
        type: 'text-image',
        title: 'Lorem ipsum dolor sit amet',
        content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur a interdum velit.
                  Quisque sit amet justo porta, varius mauris in, rutrum dui. Pellentesque eu est nec
                  ante convallis auctor. Fusce semper, justo a efficitur condimentum, orci erat cursus
                  diam, eu maximus neque velit in erat. Fusce at diam nisl. In feugiat luctus lacinia.
                  Nullam sagittis aliquet nibh id tempor. Aliquam erat volutpat. Pellentesque habitant
                  morbi tristique senectus et netus et malesuada fames ac turpis egestas.',
        image_url: 'http://docs.jboss.org/xnio/3.0.4.GA/api/org/xnio/IoFuture.png'
      },
      {
        type: 'text-slide',
        title: 'Lorem ipsum dolor sit amet',
        content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur a interdum velit.
                  Quisque sit amet justo porta, varius mauris in, rutrum dui. Pellentesque eu est nec
                  ante convallis auctor. Fusce semper, justo a efficitur condimentum, orci erat cursus
                  diam, eu maximus neque velit in erat. Fusce at diam nisl. In feugiat luctus lacinia.
                  Nullam sagittis aliquet nibh id tempor. Aliquam erat volutpat. Pellentesque habitant
                  morbi tristique senectus et netus et malesuada fames ac turpis egestas.',
        image_url: 'http://docs.jboss.org/xnio/3.0.4.GA/api/org/xnio/IoFuture.png',
        slide_url: ''
      },
      {
        type: 'text-video',
        title: 'Lorem ipsum dolor sit amet',
        content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur a interdum velit.
                  Quisque sit amet justo porta, varius mauris in, rutrum dui. Pellentesque eu est nec
                  ante convallis auctor. Fusce semper, justo a efficitur condimentum, orci erat cursus
                  diam, eu maximus neque velit in erat. Fusce at diam nisl. In feugiat luctus lacinia.
                  Nullam sagittis aliquet nibh id tempor. Aliquam erat volutpat. Pellentesque habitant
                  morbi tristique senectus et netus et malesuada fames ac turpis egestas.',
        image_url: 'http://docs.jboss.org/xnio/3.0.4.GA/api/org/xnio/IoFuture.png',
        video_url: 'https://www.youtube.com/watch?v=YykjpeuMNEk'
      }
    ]
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
