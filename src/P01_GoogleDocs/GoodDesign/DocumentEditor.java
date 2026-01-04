package P01_GoogleDocs.GoodDesign;

import P01_GoogleDocs.GoodDesign.DocumentElements.*;
import P01_GoogleDocs.GoodDesign.Persistence.IDocumentPersistence;

import java.util.List;

public class DocumentEditor {
    private Document document;
    private IDocumentPersistence documentPersistence;

    public DocumentEditor(IDocumentPersistence persistence) {
        this.document = new Document();
        this.documentPersistence = persistence;
    }

    public void addTextElement(String text) {
        document.addElement(new TextElement(text));
    }

    public void addImageElement(String filePath) {
        document.addElement(new ImageElement(filePath));
    }

    public void addHeadingElement(String text) {
        document.addElement(new HeadingElement(text));
    }

    public void addBulletListElement(List<String> listItems) {
        document.addElement(new UnorderedListElement(listItems));
    }

    public void addNextLineElement() {
        document.addElement(new NextLineElement());
    }

    public void addTabElement() {
        document.addElement(new TabSpaceElement());
    }

    public String renderDocument() {
        return document.render();
    }

    public void saveDocument() {
        String renderedContent = document.render();
        documentPersistence.save(renderedContent);
    }
}
