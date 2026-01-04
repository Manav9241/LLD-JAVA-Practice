package P01_GoogleDocs.GoodDesign.DocumentElements;

import java.util.ArrayList;
import java.util.List;

public class UnorderedListElement implements IDocumentElement{
    private List<String> listItems;

    public UnorderedListElement(List<String> listItems) {
        this.listItems = new ArrayList<String>(listItems);
    }

    @Override
    public String Render() {
        StringBuilder result = new StringBuilder();
        for(String listItem: listItems) {
            result.append("- " + listItem + "\n");
        }
        return result.toString();
    }
}
