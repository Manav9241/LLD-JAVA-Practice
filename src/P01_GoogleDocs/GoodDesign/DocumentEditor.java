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
    private String cachedRenderedContent;
    private boolean isDirty;

    public DocumentEditor(IDocumentPersistence persistence, IDocumentRenderer renderer) {
        this.document = new Document();
        this.documentPersistence = persistence;
        this.documentRenderer = renderer;
        this.cachedRenderedContent = null;
        this.isDirty = true;
    }

    public void addTextElement(String text) {
        document.addElement(DocumentElementFactory.createTextElement(text));
        markDirty();
    }

    public void addImageElement(String filePath) {
        document.addElement(DocumentElementFactory.createImageElement(filePath));
        markDirty();
    }

    public void addHeadingElement(String text) {
        document.addElement(DocumentElementFactory.createHeadingElement(text));
        markDirty();
    }

    public void addBulletListElement(List<String> listItems) {
        document.addElement(DocumentElementFactory.createUnorderedListElement(listItems));
        markDirty();
    }

    public void addNextLineElement() {
        document.addElement(DocumentElementFactory.createNextLineElement());
        markDirty();
    }

    public void addTabElement() {
        document.addElement(DocumentElementFactory.createTabSpaceElement());
        markDirty();
    }

    public String renderDocument() {
        if (isDirty || cachedRenderedContent == null) {
            cachedRenderedContent = documentRenderer.render(document);
            isDirty = false;
        }
        return cachedRenderedContent;
    }

    public void saveDocument() {
        String renderedContent = renderDocument(); // Uses cached version if available
        documentPersistence.save(renderedContent);
    }

    private void markDirty() {
        isDirty = true;
    }
}
