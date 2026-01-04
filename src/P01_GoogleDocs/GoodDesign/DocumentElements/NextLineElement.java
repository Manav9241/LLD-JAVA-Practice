package P01_GoogleDocs.GoodDesign.DocumentElements;

public class NextLineElement implements IDocumentElement{
    @Override
    public String render() {
        return "\n";
    }
}
