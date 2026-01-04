package P01_GoogleDocs.BestDesign;

import P01_GoogleDocs.BestDesign.Persistence.FileStorage;
import P01_GoogleDocs.BestDesign.Rendering.PlainTextRenderer;

import java.util.Arrays;

public class BestDesignMain {
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
