package P01_GoogleDocs.BestDesign.DocumentElements;

public class TextElement implements IDocumentElement{
    private String text;

    public TextElement(String text) {
        this.text = text;
    }

    @Override
    public String render() {
        return text + "\n";
    }
}
