package P01_GoogleDocs.GoodDesign.Persistence;

public class FileStorage implements IDocumentPersistence {
    @Override
    public void save(String content) {
        // In a real implementation, this would write to a file
        System.out.println("Saving file to FileStorage...");
        System.out.println("Content length: " + content.length() + " characters");
        System.out.println("successful\n");
    }
}
