package P01_GoogleDocs.GoodDesign.DocumentElements;

public class HeadingElement implements IDocumentElement{
    private String text;

    public HeadingElement(String text) {
        this.text = text;
    }

    @Override
    public String Render() {
        return "****" + text.toUpperCase() + "****\n";
    }
}
