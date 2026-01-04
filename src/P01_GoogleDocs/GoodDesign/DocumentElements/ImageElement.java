package P01_GoogleDocs.GoodDesign.DocumentElements;

public class ImageElement implements IDocumentElement{
    private String filePath;

    public ImageElement(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String render() {
        return "[Image: " + filePath + " ]\n";
    }
}
