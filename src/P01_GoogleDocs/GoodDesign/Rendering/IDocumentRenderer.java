package P01_GoogleDocs.GoodDesign.Rendering;

import P01_GoogleDocs.GoodDesign.Document;

/**
 * Interface for document rendering strategies.
 * This allows different rendering implementations (HTML, PDF, plain text, etc.)
 * following the Open/Closed Principle.
 */
public interface IDocumentRenderer {
    String render(Document document);
}
