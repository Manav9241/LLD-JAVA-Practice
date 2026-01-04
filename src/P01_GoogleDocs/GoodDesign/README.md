# Good Design - Document Editor

## Overview
This package demonstrates a well-designed Document Editor following SOLID principles and design patterns.

## Key Design Patterns Used

### 1. **Strategy Pattern**
- **IDocumentPersistence**: Allows different storage strategies (FileStorage, CloudStorage)
- **IDocumentRenderer**: Allows different rendering strategies (PlainTextRenderer, and extensible to HTML, PDF, etc.)

### 2. **Factory Pattern**
- **DocumentElementFactory**: Centralizes element creation, decoupling DocumentEditor from concrete element implementations

### 3. **Composite Pattern**
- **Document**: Treats individual elements and compositions uniformly through the IDocumentElement interface

## SOLID Principles Applied

### Single Responsibility Principle (SRP)
- **Document**: Manages a collection of document elements
- **DocumentEditor**: Orchestrates document building operations
- **DocumentElementFactory**: Handles element creation
- **IDocumentRenderer implementations**: Handle rendering logic
- **IDocumentPersistence implementations**: Handle storage logic
- **Each Element class**: Responsible for rendering itself

### Open/Closed Principle (OCP)
- New element types can be added without modifying existing code (just implement IDocumentElement)
- New storage mechanisms can be added without modifying DocumentEditor (just implement IDocumentPersistence)
- New renderers can be added without modifying the core logic (just implement IDocumentRenderer)

### Liskov Substitution Principle (LSP)
- Any IDocumentElement implementation can be used interchangeably
- Any IDocumentPersistence implementation can be used interchangeably
- Any IDocumentRenderer implementation can be used interchangeably

### Interface Segregation Principle (ISP)
- Small, focused interfaces (IDocumentElement, IDocumentPersistence, IDocumentRenderer)
- Clients only depend on methods they actually use

### Dependency Inversion Principle (DIP)
- DocumentEditor depends on abstractions (interfaces) not concrete implementations
- High-level modules don't depend on low-level modules; both depend on abstractions

## Scalability Features

1. **Extensible Element Types**: Add new element types by implementing IDocumentElement
2. **Pluggable Storage**: Switch between FileStorage, CloudStorage, or add database storage
3. **Multiple Renderers**: Support for different output formats (plain text, HTML, PDF, etc.)
4. **Loose Coupling**: Components are independent and can be modified without affecting others

## Class Diagram

```
DocumentEditor
    ├── depends on → IDocumentPersistence (Strategy)
    ├── depends on → IDocumentRenderer (Strategy)
    └── has-a → Document
                 └── has-many → IDocumentElement
                                 ├── TextElement
                                 ├── ImageElement
                                 ├── HeadingElement
                                 ├── UnorderedListElement
                                 ├── NextLineElement
                                 └── TabSpaceElement

DocumentElementFactory (Factory)
    └── creates → IDocumentElement implementations

IDocumentPersistence
    ├── FileStorage
    └── CloudStorage

IDocumentRenderer
    └── PlainTextRenderer
```

## Usage Example

```java
DocumentEditor editor = new DocumentEditor(
    new FileStorage(),      // Choose storage strategy
    new PlainTextRenderer() // Choose rendering strategy
);

editor.addHeadingElement("Title");
editor.addTextElement("Content");
String rendered = editor.renderDocument();
editor.saveDocument();
```

## Benefits Over Bad Design

| Aspect | Bad Design | Good Design |
|--------|-----------|-------------|
| **Adding new element types** | Modify RenderDocument() with if-else | Just implement IDocumentElement |
| **Changing storage** | Modify SaveToFile() method | Pass different IDocumentPersistence |
| **Adding new output formats** | Duplicate rendering logic | Implement new IDocumentRenderer |
| **Testing** | Hard to test due to tight coupling | Easy to mock interfaces |
| **Maintainability** | High - changes ripple through code | Low - changes are isolated |
| **Scalability** | Poor - monolithic design | Excellent - modular design |
