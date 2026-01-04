package P01_GoogleDocs.BestDesign;

import P01_GoogleDocs.BestDesign.DocumentElements.IDocumentElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Document class represents a document with multiple elements.
 * It follows the Composite pattern where elements can be added
 * and rendered together.
 * 
 * DESIGN DECISION: Why Document exposes both render() and getElements()
 * ======================================================================
 * 
 * This class provides TWO ways to access document content, supporting different
 * rendering strategies:
 * 
 * 1. render() - Simple aggregation approach
 *    - Used by PlainTextRenderer
 *    - Just concatenates each element's render() output
 *    - Document knows how to render itself in its "natural" format
 *    - Good for: Simple formats that don't need structural awareness
 * 
 * 2. getElements() - Element-level access approach
 *    - Used by HTMLRenderer, PDFRenderer, etc.
 *    - Provides access to individual elements for custom rendering
 *    - Renderer can inspect element types and apply format-specific logic
 *    - Good for: Complex formats requiring structural transformation
 * 
 * WHY BOTH EXIST:
 * - Separation of Concerns: Document is a data structure, not a rendering engine
 * - Strategy Pattern: Different renderers use different approaches
 * - Open/Closed Principle: Add new renderers without modifying Document
 * - Flexibility: Simple renderers use render(), complex ones use getElements()
 * 
 * EXAMPLE:
 * - PlainTextRenderer: calls document.render() - simple delegation
 * - HTMLRenderer: calls document.getElements() and wraps each in HTML tags
 * - PDFRenderer: calls document.getElements() and uses PDF layout library
 * 
 * This "cost of extensibility" means some methods appear unused initially,
 * but enable powerful rendering capabilities without coupling Document to
 * specific output formats.
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
     * 
     * PURPOSE: Enables sophisticated renderers to access individual elements
     * for custom formatting logic.
     * 
     * USAGE EXAMPLES:
     * - HTMLRenderer: Wraps TextElement in <p>, HeadingElement in <h1>
     * - PDFRenderer: Uses element types to determine font size and style
     * - MarkdownRenderer: Converts HeadingElement to "# heading" syntax
     * - PreviewRenderer: Truncates long elements, adds ellipsis
     * 
     * DESIGN NOTE: Returns unmodifiable list to prevent external modification
     * while still allowing read access. This maintains encapsulation while
     * providing necessary flexibility for renderers.
     * 
     * @return Unmodifiable list of document elements
     */
    public List<IDocumentElement> getElements() {
        return Collections.unmodifiableList(documentElements);
    }

    /**
     * Default rendering implementation.
     * 
     * PURPOSE: Provides simple concatenation of all element outputs.
     * This is the "natural" representation of the document.
     * 
     * USAGE EXAMPLES:
     * - PlainTextRenderer: Delegates directly to this method
     * - Internal debugging: Quick string representation
     * - Simple output scenarios: When no special formatting needed
     * 
     * DESIGN NOTE: This method doesn't make Document a "renderer" - it's
     * simply the default serialization. Real rendering strategies use
     * IDocumentRenderer interface for flexibility.
     * 
     * @return String representation by concatenating all elements
     */
    public String render() {
        StringBuilder result = new StringBuilder();
        for(IDocumentElement element: documentElements) {
            result.append(element.render());
        }
        return result.toString();
    }
}
