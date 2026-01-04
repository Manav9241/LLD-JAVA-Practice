package P01_GoogleDocs.BestDesign.Rendering;

import P01_GoogleDocs.BestDesign.Document;
import P01_GoogleDocs.BestDesign.DocumentElements.*;

/**
 * HTMLRenderer converts document elements into HTML format.
 * 
 * DESIGN PATTERN: Strategy Pattern
 * =================================
 * This is an alternative rendering strategy to PlainTextRenderer.
 * Both implement IDocumentRenderer interface, enabling runtime selection
 * of output format without changing Document or DocumentEditor code.
 * 
 * WHY THIS RENDERER EXISTS:
 * =========================
 * This demonstrates why Document.getElements() is necessary and why the
 * Strategy Pattern for rendering is superior to having Document know about
 * multiple output formats.
 * 
 * COMPARISON WITH PlainTextRenderer:
 * ===================================
 * - PlainTextRenderer: Calls document.render() - simple delegation
 * - HTMLRenderer: Calls document.getElements() - needs element-level control
 * 
 * WHY getElements() APPROACH:
 * ===========================
 * HTML requires different tags for different element types:
 * - TextElement -> <p>text</p>
 * - HeadingElement -> <h1>heading</h1>
 * - ImageElement -> <img src="path" alt="image"/>
 * - UnorderedListElement -> <ul><li>item</li></ul>
 * 
 * If we used document.render(), we'd get plain text with no HTML structure.
 * By accessing getElements(), we can inspect each element's type and apply
 * appropriate HTML formatting.
 * 
 * ARCHITECTURE BENEFITS:
 * ======================
 * 1. Document stays format-agnostic (doesn't know about HTML)
 * 2. Can add PDFRenderer, MarkdownRenderer without touching Document
 * 3. Easy to unit test (mock Document.getElements())
 * 4. Runtime flexibility (swap renderer based on user preference)
 * 
 * USAGE EXAMPLE:
 * ==============
 * DocumentEditor editor = new DocumentEditor(
 *     new FileStorage(),
 *     new HTMLRenderer()  // Instead of PlainTextRenderer
 * );
 * editor.addHeadingElement("Title");
 * editor.addTextElement("Content");
 * String html = editor.renderDocument(); // Returns full HTML
 * 
 * OUTPUT:
 * =======
 * <!DOCTYPE html>
 * <html>
 * <head><title>Document</title></head>
 * <body>
 *   <h1>TITLE</h1>
 *   <p>Content</p>
 * </body>
 * </html>
 */
public class HTMLRenderer implements IDocumentRenderer {
    
    /**
     * Renders document as HTML by inspecting element types.
     * 
     * ALGORITHM:
     * 1. Create HTML structure (DOCTYPE, html, head, body tags)
     * 2. Iterate through document.getElements()
     * 3. Use instanceof to determine element type
     * 4. Apply appropriate HTML tag for each type
     * 5. Close all HTML tags properly
     * 
     * TYPE INSPECTION RATIONALE:
     * ==========================
     * We use instanceof because each element type needs different HTML markup:
     * - Cannot use polymorphism alone (render() returns plain text)
     * - Could use Visitor Pattern, but instanceof is simpler for this use case
     * - This is acceptable because HTMLRenderer's job IS to know about element types
     * 
     * ALTERNATIVE APPROACHES CONSIDERED:
     * ==================================
     * 1. Add renderAsHTML() to each element:
     *    - Violates SRP (elements know about HTML)
     *    - Hard to add new formats (PDF, Markdown, etc.)
     * 
     * 2. Visitor Pattern:
     *    - More complex, requires accept() methods in all elements
     *    - Overkill for simple type-based rendering
     * 
     * 3. Current approach (instanceof):
     *    - Simple, clear, localized in renderer
     *    - Easy to understand and maintain
     *    - Renderer owns format knowledge (appropriate)
     * 
     * @param document The document to render
     * @return HTML string representation
     */
    @Override
    public String render(Document document) {
        StringBuilder html = new StringBuilder();
        
        // HTML document structure
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>Document</title>\n");
        html.append("  <style>\n");
        html.append("    body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("    h1 { color: #333; border-bottom: 2px solid #333; }\n");
        html.append("    p { line-height: 1.6; }\n");
        html.append("    img { max-width: 100%; height: auto; }\n");
        html.append("    ul { margin-left: 20px; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n\n");
        
        // Render each element with appropriate HTML tags
        // This is where getElements() becomes essential!
        for (IDocumentElement element : document.getElements()) {
            if (element instanceof HeadingElement) {
                // Heading gets <h1> tag
                String content = element.render().replace("****", "").trim();
                html.append("  <h1>").append(escapeHtml(content)).append("</h1>\n");
                
            } else if (element instanceof ImageElement) {
                // Image gets <img> tag with proper src attribute
                String content = element.render();
                String imagePath = content.replace("[Image: ", "").replace(" ]\n", "").trim();
                html.append("  <img src=\"").append(escapeHtml(imagePath))
                    .append("\" alt=\"Image\">\n");
                
            } else if (element instanceof UnorderedListElement) {
                // List gets <ul> and <li> tags
                html.append("  <ul>\n");
                String content = element.render();
                String[] items = content.split("\n");
                for (String item : items) {
                    if (item.trim().startsWith("- ")) {
                        String listItem = item.substring(2).trim();
                        html.append("    <li>").append(escapeHtml(listItem)).append("</li>\n");
                    }
                }
                html.append("  </ul>\n");
                
            } else if (element instanceof NextLineElement) {
                // Line break gets <br>
                html.append("  <br>\n");
                
            } else if (element instanceof TabSpaceElement) {
                // Tab gets non-breaking spaces
                html.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                
            } else if (element instanceof TextElement) {
                // Regular text gets <p> tag
                String content = element.render().trim();
                html.append("  <p>").append(escapeHtml(content)).append("</p>\n");
                
            } else {
                // Fallback for unknown element types
                html.append("  <p>").append(escapeHtml(element.render())).append("</p>\n");
            }
        }
        
        html.append("\n</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    /**
     * Escapes HTML special characters to prevent injection and display issues.
     * 
     * SECURITY NOTE: Always escape user content in HTML to prevent XSS attacks.
     * 
     * @param text Text to escape
     * @return HTML-safe text
     */
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
