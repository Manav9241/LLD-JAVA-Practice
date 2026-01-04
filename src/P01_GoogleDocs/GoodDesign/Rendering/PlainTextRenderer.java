package P01_GoogleDocs.GoodDesign.Rendering;

import P01_GoogleDocs.GoodDesign.Document;

/**
 * Default plain text renderer implementation.
 * Delegates to the Document's internal render method.
 */
public class PlainTextRenderer implements IDocumentRenderer {
    @Override
    public String render(Document document) {
        return document.render();
    }
}
