//= require showdown

function compile_markdown() {
    var converter = new showdown.Converter(),
        text = $('.markdown .markdown-data').val(),
        html = converter.makeHtml(text),
        $markdown_preview = $('.markdown .markdown-body');
    $markdown_preview.empty();
    $markdown_preview.append(html);
}