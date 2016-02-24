Rails.application.routes.draw do

  get 'search/:content' => 'search#search'

  get 'photography'     => 'photography#index'

  get 'good_reads'      => 'good_reads#index'

  get 'good_reads/:id'  => 'good_reads#show'

  get 'topics'          => 'good_reads#topics'

  post 'contact'        => 'home#contact'

  root 'home#index'

end
