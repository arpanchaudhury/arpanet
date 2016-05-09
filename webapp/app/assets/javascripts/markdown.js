//= require showdown

function compile_markdown(element) {
    var converter = new showdown.Converter();
        converter.setOption('tables', true);
        converter.setOption('tasklists', true);
    var input_md_text = element.find('.markdown .markdown-data').val(),
        converted_md_html = converter.makeHtml(input_md_text),
        markdown_preview = element.find('.markdown .markdown-body');
    markdown_preview.empty();
    markdown_preview.append(converted_md_html);
}