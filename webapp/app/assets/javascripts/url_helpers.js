function add_parameter_to_URL(_url, param) {
    _url += (_url.split('?')[1] ? '&' : '?') + param;
    return _url;
}

function remove_parameter_from_URL(_url, param) {
    var url_parts = _url.split('?');
    if (url_parts.length == 1) return _url;
    else {
        var query_params = url_parts[1].split(/[&]/g);
        var updated_query_params = $.grep(query_params, function (query_param) {
            return query_param != param;
        });
        if (updated_query_params.length == 0) return url_parts[0];
        else return url_parts[0] + '?' + updated_query_params.join('&');
    }
}

function remove_all_params(url) {
    return url.split('?')[0];
}
