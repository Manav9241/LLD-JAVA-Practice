package P01_GoogleDocs.GoodDesign.Persistence;

public class FileStorage implements IDocumentPersistence {
    @Override
    public void Save() {
        System.out.println("Saving file to FileStorage...\nsuccessful\n");
    }
}
