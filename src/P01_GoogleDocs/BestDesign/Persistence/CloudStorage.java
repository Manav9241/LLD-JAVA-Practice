package P01_GoogleDocs.BestDesign.Persistence;

public class CloudStorage implements IDocumentPersistence {
    @Override
    public void save(String content) {
        // In a real implementation, this would upload to cloud storage
        System.out.println("Uploading document to CloudStorage...");
        System.out.println("Content length: " + content.length() + " characters");
        System.out.println("successful\n");
    }
}
