package P01_GoogleDocs.GoodDesign;

import P01_GoogleDocs.GoodDesign.DocumentElements.*;
import P01_GoogleDocs.GoodDesign.Persistence.IDocumentPersistence;

import java.util.List;

public class DocumentEditor {
    private Document document;
    private String renderedDocument;
    private int renderedDocumentSize;
    private IDocumentPersistence documentPersistence;

    public DocumentEditor(IDocumentPersistence persistence) {
        this.document = new Document();
        this.renderedDocument = "";
        this.renderedDocumentSize = 0;
        this.documentPersistence = persistence;
    }

    public void AddTextElement(String text) {
        document.AddElement(new TextElement(text));
    }

    public void AddImageElement(String filePath) {
        document.AddElement(new ImageElement(filePath));
    }

    public void AddHeadingElement(String text) {
        document.AddElement(new HeadingElement(text));
    }

    public void AddBulletListElement(List<String> listItems) {
        document.AddElement(new UnorderedListElement(listItems));
    }

    public void AddNextLineElement() {
        document.AddElement(new NextLineElement());
    }

    public void AddTabElement() {
        document.AddElement(new TabSpaceElement());
    }

    public String RenderDocument() {
        return document.Render();
    }

    public void SaveDocument() {
        documentPersistence.Save();
    }
}
