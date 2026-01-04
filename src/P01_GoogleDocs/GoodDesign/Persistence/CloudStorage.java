package P01_GoogleDocs.GoodDesign.Persistence;

public class CloudStorage implements IDocumentPersistence {
    @Override
    public void Save() {
        System.out.println("Uploading document to CloudStorage...\nsuccessful\n");
    }
}
