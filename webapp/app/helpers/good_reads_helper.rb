module GoodReadsHelper
  def get_blog_post_timestamp(blog_post)
    blog_post['timestamp']
  end

  def get_blog_post_url(blog_post)
    "/good_reads/#{blog_post['_id']}"
  end

  def get_blog_post_title(blog_post)
    blog_post['title']
  end

  def get_blog_post_content(blog_post)
    blog_post['content']
  end

  def get_blog_post_type(blog_post)
    blog_post['contentType']
  end

  def get_blog_post_topics(blog_post, count)
    blog_post['topics'].take(count).join(', ')
  end

  def get_blog_post_markdown(blog_post)
    conn = Faraday.new "#{Rails.configuration.x.api.url}/write-ups/#{blog_post['_id']}/markdown"
    conn.get.body
  end

  def get_blog_post_code(blog_post)
    blog_post['code']
  end

  def get_blog_post_image_url(blog_post)
    "#{Rails.configuration.x.api.url}/images/public-image/#{blog_post['imageId']}"
  end

  def get_blog_post_video_url(blog_post)
    blog_post['videoUrl']
  end

  def get_blog_post_slide_url(blog_post)
    blog_post['slideUrl']
    end

  def get_blog_post_poster_image_url(blog_post)
    blog_post['posterImageUrl']
  end
end