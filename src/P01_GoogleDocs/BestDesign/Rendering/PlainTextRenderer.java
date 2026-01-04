package P01_GoogleDocs.BestDesign.Rendering;

import P01_GoogleDocs.BestDesign.Document;

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
