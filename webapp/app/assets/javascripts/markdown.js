//= require showdown

function compile_markdown() {
    var converter = new showdown.Converter(),
        text = $('.markdown .markdown-data').text(),
        html = converter.makeHtml(text);
    $('.markdown .markdown-body').append(html);
}