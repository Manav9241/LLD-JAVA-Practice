package P01_GoogleDocs.BadDesign;

public class BadClientMain {
    public static void main(String args[]) {
        DocumentEditor documentEditor = new DocumentEditor();
        documentEditor.AddTextElement("Hello World");
        documentEditor.AddImageElement("picture.jpg");
        documentEditor.AddTextElement("New Text Field");
        documentEditor.AddImageElement("photo.png");
        documentEditor.AddTextElement("This is a document editor");

        System.out.println(documentEditor.RenderDocument());

        documentEditor.SaveToFile("document.txt");
    }
}
