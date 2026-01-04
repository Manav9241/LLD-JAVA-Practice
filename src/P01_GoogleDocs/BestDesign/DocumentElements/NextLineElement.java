package P01_GoogleDocs.BestDesign.DocumentElements;

public class NextLineElement implements IDocumentElement{
    @Override
    public String render() {
        return "\n";
    }
}
