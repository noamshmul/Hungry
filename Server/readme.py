import markdown

def read_readme() -> str:
    with open('../readme.md', 'r') as f:
        readme = f.read()
    return readme

def get_readme():
    try:
        readme_md = read_readme()
        readme_html = markdown.markdown(readme_md)
    except Exception as e:
        readme_html = f'<b>ERR: {e}</b>'
    print("readme:",readme_html)

    html_content = f"""
    <html>
        <head>
            <title>Hungry</title>
        </head>
        <body>
            {readme_html}
        </body>
    </html>
    """
    return html_content