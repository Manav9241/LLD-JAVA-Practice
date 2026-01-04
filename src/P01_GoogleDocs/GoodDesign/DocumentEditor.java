package P01_GoogleDocs.GoodDesign;

import P01_GoogleDocs.GoodDesign.DocumentElements.DocumentElementFactory;
import P01_GoogleDocs.GoodDesign.Persistence.IDocumentPersistence;
import P01_GoogleDocs.GoodDesign.Rendering.IDocumentRenderer;

import java.util.List;

/**
 * DocumentEditor manages document creation and persistence.
 * It uses:
 * - DocumentElementFactory to create elements (Factory Pattern)
 * - IDocumentRenderer for rendering (Strategy Pattern)
 * - IDocumentPersistence for saving (Strategy Pattern)
 * This design follows SRP, OCP, and DIP from SOLID principles.
 */
public class DocumentEditor {
    private Document document;
    private IDocumentPersistence documentPersistence;
    private IDocumentRenderer documentRenderer;

    public DocumentEditor(IDocumentPersistence persistence, IDocumentRenderer renderer) {
        this.document = new Document();
        this.documentPersistence = persistence;
        this.documentRenderer = renderer;
    }

    public void addTextElement(String text) {
        document.addElement(DocumentElementFactory.createTextElement(text));
    }

    public void addImageElement(String filePath) {
        document.addElement(DocumentElementFactory.createImageElement(filePath));
    }

    public void addHeadingElement(String text) {
        document.addElement(DocumentElementFactory.createHeadingElement(text));
    }

    public void addBulletListElement(List<String> listItems) {
        document.addElement(DocumentElementFactory.createUnorderedListElement(listItems));
    }

    public void addNextLineElement() {
        document.addElement(DocumentElementFactory.createNextLineElement());
    }

    public void addTabElement() {
        document.addElement(DocumentElementFactory.createTabSpaceElement());
    }

    public String renderDocument() {
        return documentRenderer.render(document);
    }

    public void saveDocument() {
        String renderedContent = documentRenderer.render(document);
        documentPersistence.save(renderedContent);
    }
}
