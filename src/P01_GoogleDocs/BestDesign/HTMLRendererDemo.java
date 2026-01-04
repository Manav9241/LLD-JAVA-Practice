package P01_GoogleDocs.BestDesign;

import P01_GoogleDocs.BestDesign.Persistence.FileStorage;
import P01_GoogleDocs.BestDesign.Rendering.HTMLRenderer;
import P01_GoogleDocs.BestDesign.Rendering.PlainTextRenderer;

import java.util.Arrays;

/**
 * Demonstration of HTMLRenderer vs PlainTextRenderer.
 * 
 * This shows why the Strategy Pattern for rendering is powerful:
 * - Same document
 * - Same DocumentEditor API
 * - Different output formats just by swapping renderer
 */
public class HTMLRendererDemo {
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("DEMONSTRATION: Why getElements() and Strategy Pattern Matter");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Create document with PlainTextRenderer
        System.out.println("1. Creating document with PlainTextRenderer:");
        System.out.println("-".repeat(80));
        DocumentEditor plainTextEditor = new DocumentEditor(
            new FileStorage(),
            new PlainTextRenderer()
        );
        
        plainTextEditor.addHeadingElement("Welcome to BestDesign");
        plainTextEditor.addNextLineElement();
        plainTextEditor.addTextElement("This document demonstrates the power of the Strategy Pattern.");
        plainTextEditor.addImageElement("architecture-diagram.png");
        plainTextEditor.addTextElement("Key Features:");
        plainTextEditor.addBulletListElement(Arrays.asList(
            "Factory Pattern for element creation",
            "Strategy Pattern for rendering",
            "Pluggable persistence layer"
        ));
        
        String plainText = plainTextEditor.renderDocument();
        System.out.println(plainText);
        System.out.println();
        
        // Create same document with HTMLRenderer
        System.out.println("2. Creating SAME document with HTMLRenderer:");
        System.out.println("-".repeat(80));
        DocumentEditor htmlEditor = new DocumentEditor(
            new FileStorage(),
            new HTMLRenderer()  // Only this line changed!
        );
        
        htmlEditor.addHeadingElement("Welcome to BestDesign");
        htmlEditor.addNextLineElement();
        htmlEditor.addTextElement("This document demonstrates the power of the Strategy Pattern.");
        htmlEditor.addImageElement("architecture-diagram.png");
        htmlEditor.addTextElement("Key Features:");
        htmlEditor.addBulletListElement(Arrays.asList(
            "Factory Pattern for element creation",
            "Strategy Pattern for rendering",
            "Pluggable persistence layer"
        ));
        
        String html = htmlEditor.renderDocument();
        System.out.println(html);
        System.out.println();
        
        System.out.println("=".repeat(80));
        System.out.println("EXPLANATION:");
        System.out.println("=".repeat(80));
        System.out.println("1. PlainTextRenderer uses document.render() - simple delegation");
        System.out.println("2. HTMLRenderer uses document.getElements() - needs type-specific formatting");
        System.out.println("3. Same API, different output - that's the Strategy Pattern!");
        System.out.println("4. Document doesn't know about HTML - Separation of Concerns!");
        System.out.println("5. Easy to add PDFRenderer, MarkdownRenderer - Open/Closed Principle!");
        System.out.println("=".repeat(80));
    }
}
