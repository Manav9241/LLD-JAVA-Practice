package P01_GoogleDocs.GoodDesign.DocumentElements;

public class TextElement implements IDocumentElement{
    private String text;

    public TextElement(String text) {
        this.text = text;
    }

    @Override
    public String Render() {
        return text + "\n";
    }
}
