import markdown
from log import logger

readme_html = None

def read_readme() -> str:
    with open('../readme.md', 'r', encoding="utf8") as f:
        readme = f.read()
    return readme

def get_readme():
    global readme_html
    if not readme_html:
        try:
            readme_md = read_readme()
            readme_html = markdown.markdown(readme_md)
            logger.info("Pull readme.md")
        except Exception as e:
            readme_html = f'<b>ERR: {e}</b>'
            logger.error("Couldn't get readme.md - %s", e)
        

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