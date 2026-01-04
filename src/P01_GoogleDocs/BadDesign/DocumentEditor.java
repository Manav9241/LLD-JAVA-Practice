package P01_GoogleDocs.BadDesign;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentEditor {
    private List<String> documentElements;
    private String renderedString;
    private int renderedStringSize;

    public DocumentEditor() {
        this.documentElements = new ArrayList<String>();
        this.renderedString = "";
        renderedStringSize = 0;
    }

    public void AddTextElement(String text) {
        documentElements.add(text);
    }

    public void AddImageElement(String filePath) {
        documentElements.add(filePath);
    }

    public String RenderDocument() {
        if(renderedStringSize != documentElements.size()) {
            StringBuilder result = new StringBuilder();
            for(String element: documentElements) {
                if(element.endsWith(".jpg") || element.endsWith(".png")) {
                    result.append("[Image: ").append(element).append(" ]\n");
                } else {
                    result.append(element + "\n");
                }
            }
            renderedString = result.toString();
            renderedStringSize = documentElements.size();
        }
        return renderedString;
    }

    public void SaveToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(RenderDocument());
            writer.close();
            System.out.println("Document saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error: Unable to open file for Writing");
        }
    }
}
