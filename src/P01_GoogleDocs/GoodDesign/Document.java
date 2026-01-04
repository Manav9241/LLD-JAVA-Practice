package P01_GoogleDocs.GoodDesign;

import P01_GoogleDocs.GoodDesign.DocumentElements.IDocumentElement;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private List<IDocumentElement> documentElements;

    public Document() {
        this.documentElements = new ArrayList<IDocumentElement>();
    }

    public void AddElement(IDocumentElement element) {
        documentElements.add(element);
    }

    public String Render() {
        StringBuilder result = new StringBuilder();
        for(IDocumentElement element: documentElements) {
            result.append(element.Render());
        }
        return result.toString();
    }
}
