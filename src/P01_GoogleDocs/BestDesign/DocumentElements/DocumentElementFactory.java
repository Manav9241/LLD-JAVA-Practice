package P01_GoogleDocs.BestDesign.DocumentElements;

import java.util.List;

/**
 * Factory class for creating document elements.
 * This separates the responsibility of element creation from the DocumentEditor,
 * following the Single Responsibility Principle.
 */
public class DocumentElementFactory {
    
    public static IDocumentElement createTextElement(String text) {
        return new TextElement(text);
    }
    
    public static IDocumentElement createImageElement(String filePath) {
        return new ImageElement(filePath);
    }
    
    public static IDocumentElement createHeadingElement(String text) {
        return new HeadingElement(text);
    }
    
    public static IDocumentElement createUnorderedListElement(List<String> listItems) {
        return new UnorderedListElement(listItems);
    }
    
    public static IDocumentElement createNextLineElement() {
        return new NextLineElement();
    }
    
    public static IDocumentElement createTabSpaceElement() {
        return new TabSpaceElement();
    }
}
