package P01_GoogleDocs.BestDesign.DocumentElements;

public class HeadingElement implements IDocumentElement{
    private String text;

    public HeadingElement(String text) {
        this.text = text;
    }

    @Override
    public String render() {
        return "****" + text.toUpperCase() + "****\n";
    }
}
