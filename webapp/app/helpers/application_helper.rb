module ApplicationHelper

  def get_next_page_url(pager, path, other_params)
    page_start = pager[:page_start].to_i
    page_length = pager[:page_length].to_i
    pages = (pager[:count].to_f / page_length).ceil.to_i
    if page_start < pages - 1
      others = other_params.collect { |other_param_key, other_param_value|
        (other_param_value.collect { |value| "#{other_param_key}[]=#{value}" }).join('&')
      }.join('&')
      if others.empty?
        "#{path}?page-start=#{page_start + 1}&page-length=#{page_length}"
      else
        "#{path}?page-start=#{page_start + 1}&page-length=#{page_length}&#{others}"
      end
    else
      ''
    end
  end

  def get_previous_page_url(pager, path, other_params)
    page_start = pager[:page_start].to_i
    page_length = pager[:page_length].to_i
    if page_start > 0
      others = other_params.collect { |other_param_key, other_param_value|
        (other_param_value.collect { |value| "#{other_param_key}[]=#{value}" }).join('&')
      }.join('&')
      if others.empty?
        "#{path}?page-start=#{page_start - 1}&page-length=#{page_length}"
      else
        "#{path}?page-start=#{page_start - 1}&page-length=#{page_length}&#{others}"
      end
    else
      ''
    end
  end

  def is_device
    ua = request.user_agent

    tmp = ua.split('[FBAN')
    if tmp[1]
      ua = tmp[0]
    end

    tmp = ua.split('Twitter')
    if tmp[1]
      ua = tmp[0]
    end

    apple_phone = /iPhone/i
    apple_ipod = /iPod/i
    apple_tablet = /iPad/i
    android_phone = /(?=.*\bAndroid\b)(?=.*\bMobile\b)/i
    android_tablet = /Android/i
    amazon_phone = /(?=.*\bAndroid\b)(?=.*\bSD4930UR\b)/i
    amazon_tablet = /(?=.*\bAndroid\b)(?=.*\b(?:KFOT|KFTT|KFJWI|KFJWA|KFSOWI|KFTHWI|KFTHWA|KFAPWI|KFAPWA|KFARWI|KFASWI|KFSAWI|KFSAWA)\b)/i
    windows_phone = /IEMobile/i
    windows_tablet = /(?=.*\bWindows\b)(?=.*\bARM\b)/i
    other_blackberry = /BlackBerry/i
    other_blackberry_10 = /BB10/i
    other_opera = /Opera Mini/i
    other_chrome = /(CriOS|Chrome)(?=.*\bMobile\b)/i
    other_firefox = /(?=.*\bFirefox\b)(?=.*\bMobile\b)/i
    seven_inch = /(?:Nexus 7|BNTV250|Kindle Fire|Silk|GT-P1000)/i

    apple_phone === ua or
    apple_ipod === ua or
    apple_tablet === ua or
    android_phone === ua or
    android_tablet === ua or
    amazon_phone === ua or
    amazon_tablet === ua or
    windows_phone === ua or
    windows_tablet === ua or
    other_blackberry === ua or
    other_blackberry_10 === ua or
    other_opera === ua or
    other_chrome === ua or
    other_firefox === ua or
    seven_inch === ua
  end

  def get_first_content_index(pager)
    pager[:page_start] * pager[:page_length] + 1
  end

  def get_last_content_index(pager)
    if !last_page?(pager)
      pager[:page_start] * pager[:page_length] + pager[:page_length]
    else
      pager[:page_start] * pager[:page_length] + pager[:count] - pager[:page_start] * pager[:page_length]
    end
  end

  private def last_page?(pager)
    pager[:page_start] * pager[:page_length] + pager[:page_length] >= pager[:count]
  end
end
