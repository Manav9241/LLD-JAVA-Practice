package P01_GoogleDocs.BestDesign.Rendering;

import P01_GoogleDocs.BestDesign.Document;
import P01_GoogleDocs.BestDesign.DocumentElements.*;

/**
 * HTMLRenderer converts document elements into HTML format.
 * 
 * This demonstrates why Document.getElements() is necessary - different
 * element types need different HTML tags (h1 for headings, p for text, etc.)
 * 
 * While PlainTextRenderer just calls document.render(), HTMLRenderer needs
 * element-level access to apply type-specific formatting.
 */
public class HTMLRenderer implements IDocumentRenderer {
    
    @Override
    public String render(Document document) {
        StringBuilder html = new StringBuilder();
        for (IDocumentElement elem : document.getElements()) {
            if (elem instanceof HeadingElement)
                html.append("<h1>").append(elem.render()).append("</h1>");
            else if (elem instanceof ImageElement)
                html.append("<img>");
            else
                html.append("<p>").append(elem.render()).append("</p>");
        }
        return html.toString();
    }
}
