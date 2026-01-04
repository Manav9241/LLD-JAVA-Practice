package P01_GoogleDocs.GoodDesign;

import P01_GoogleDocs.GoodDesign.Persistence.FileStorage;
import P01_GoogleDocs.GoodDesign.Rendering.PlainTextRenderer;

import java.util.Arrays;

public class GoodDesignMain {
    public static void main(String[] args) {
        DocumentEditor documentEditor = new DocumentEditor(
            new FileStorage(), 
            new PlainTextRenderer()
        );

        documentEditor.addHeadingElement("Document_Heading");
        documentEditor.addNextLineElement();
        documentEditor.addImageElement("topic.jpg");
        documentEditor.addTextElement("Image Description Text");
        documentEditor.addTabElement();
        documentEditor.addTextElement("To Do List");
        documentEditor.addBulletListElement(Arrays.asList("Drink Water", "Grocery Shopping", "Daily Study Tracker"));

        System.out.println(documentEditor.renderDocument());
        documentEditor.saveDocument();
    }
}
