class StaticAssetController < ApplicationController

  def public_image
    image_id   = params[:image_id]
    max_width  = params[:maxWidth]
    max_height = params[:maxHeight]

    if max_height && max_width
      uri = "#{Rails.configuration.x.api.url}/images/public-image/#{image_id}?maxWidth=#{max_width}&maxHeight=#{max_height}"
    else
      uri = "#{Rails.configuration.x.api.url}/images/public-image/#{image_id}"
    end

    conn = Faraday.new uri

    File.open("/#{Rails.configuration.x.tmp}/#{image_id}.jpg", 'wb') { |fp|
      fp.write(conn.get.body)
      send_file fp, :type => 'image/jpeg', :disposition => 'inline'
    }
  end

  def photography
    image_id   = params[:image_id]
    max_width  = params[:maxWidth]
    max_height = params[:maxHeight]

    if max_height && max_width
      uri = "#{Rails.configuration.x.api.url}/images/photography/#{image_id}?maxWidth=#{max_width}&maxHeight=#{max_height}"
    else
      uri = "#{Rails.configuration.x.api.url}/images/photography/#{image_id}"
    end

    conn = Faraday.new uri

    File.open("#{Rails.configuration.x.tmp}/#{image_id}.jpg", 'wb') { |fp|
      fp.write(conn.get.body)
      send_file fp, :type => 'image/jpeg', :disposition => 'inline'
    }
  end

  def resume
    conn = Faraday.new "#{Rails.configuration.x.api.url}/resume"

    File.open("#{Rails.configuration.x.tmp}/resume.pdf", 'wb') { |fp|
      fp.write(conn.get.body)
      send_file fp, :type => 'application/pdf', :disposition => 'inline'
    }
  end

end