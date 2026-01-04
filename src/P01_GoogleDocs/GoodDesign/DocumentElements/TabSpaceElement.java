package P01_GoogleDocs.GoodDesign.DocumentElements;

public class TabSpaceElement implements IDocumentElement{
    @Override
    public String render() {
        return "\t";
    }
}
