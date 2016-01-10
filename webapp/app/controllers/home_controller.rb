class HomeController < ApplicationController
  def index
    @profile_image =  "#{Rails.configuration.x.api.url}/images/public-image/test"
    @body_paragraph_one =
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur a interdum velit. Quisque sit amet justo porta, varius mauris in, rutrum dui.
        Pellentesque eu est nec ante convallis auctor. Fusce semper, justo a efficitur condimentum, orci erat cursus diam, eu maximus neque velit in erat.
        Fusce at diam nisl. In feugiat luctus lacinia. Nullam sagittis aliquet nibh id tempor. Aliquam erat volutpat. Pellentesque habitant morbi tristique
        senectus et netus et malesuada fames ac turpis egestas. Duis sed lacinia nulla. Maecenas molestie urna nibh, eu imperdiet magna facilisis ut.'

    @body_paragraph_two =
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur a interdum velit. Quisque sit amet justo porta, varius mauris in, rutrum dui.
        Pellentesque eu est nec ante convallis auctor. Fusce semper, justo a efficitur condimentum, orci erat cursus diam, eu maximus neque velit in erat.
        Fusce at diam nisl. In feugiat luctus lacinia. Nullam sagittis aliquet nibh id tempor. Aliquam erat volutpat. Pellentesque habitant morbi tristique
        senectus et netus et malesuada fames ac turpis egestas. Duis sed lacinia nulla. Maecenas molestie urna nibh, eu imperdiet magna facilisis ut.'
  end

  def contact
    ContactMailer.personal_mail('test-user@xmail.com', @body_paragraph_one).deliver_now
  end
end
