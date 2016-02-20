//= require showdown

function compile_markdown(element) {
    var converter = new showdown.Converter(),
        text = element.find('.markdown .markdown-data').val(),
        html = converter.makeHtml(text),
        $markdown_preview = element.find('.markdown .markdown-body');
    $markdown_preview.empty();
    $markdown_preview.append(html);
}