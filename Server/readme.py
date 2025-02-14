import markdown
from log import logger

readme_html = None

def read_readme() -> str:
    with open('../readme.md', 'r') as f:
        readme = f.read()
    return readme

def get_readme():
    global readme_html
    if not readme_html:
        try:
            readme_md = read_readme()
            readme_html = markdown.markdown(readme_md)
        except Exception as e:
            readme_html = f'<b>ERR: {e}</b>'
        logger.info("Pull readme.md")

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