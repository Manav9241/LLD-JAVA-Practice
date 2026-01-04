package P01_GoogleDocs.BestDesign;

import P01_GoogleDocs.BestDesign.DocumentElements.IDocumentElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Document class represents a document with multiple elements.
 * It follows the Composite pattern where elements can be added
 * and rendered together.
 */
public class Document {
    private List<IDocumentElement> documentElements;

    public Document() {
        this.documentElements = new ArrayList<IDocumentElement>();
    }

    public void addElement(IDocumentElement element) {
        documentElements.add(element);
    }

    /**
     * Returns an unmodifiable view of the document elements.
     * This allows renderers to access elements without modifying them.
     */
    public List<IDocumentElement> getElements() {
        return Collections.unmodifiableList(documentElements);
    }

    /**
     * Default rendering implementation.
     * Iterates through all elements and calls their render methods.
     */
    public String render() {
        StringBuilder result = new StringBuilder();
        for(IDocumentElement element: documentElements) {
            result.append(element.render());
        }
        return result.toString();
    }
}
