package P01_GoogleDocs.GoodDesign;

import P01_GoogleDocs.GoodDesign.Persistence.FileStorage;

import java.util.Arrays;

public class GoodDesignMain {
    public static void main(String[] args) {
        DocumentEditor documentEditor = new DocumentEditor(new FileStorage());

        documentEditor.AddHeadingElement("Document_Heading");
        documentEditor.AddNextLineElement();
        documentEditor.AddImageElement("topic.jpg");
        documentEditor.AddTextElement("Image Description Text");
        documentEditor.AddTabElement();
        documentEditor.AddTextElement("To Do List");
        documentEditor.AddBulletListElement(Arrays.asList("Drink Water", "Groccery Shopping", "Daily Study Tracker"));

        System.out.println(documentEditor.RenderDocument());
        documentEditor.SaveDocument();
    }
}
