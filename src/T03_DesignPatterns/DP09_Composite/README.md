# Composite Design Pattern — Structural (GoF)

> **"Treat one and many the same way."**

Compose objects into **tree structures** so clients treat individual objects and compositions **uniformly** — no `instanceof` checks needed.

---

## The Problem (File Explorer)

You have **Files** (leaves) and **Folders** (containers that hold files _and_ other folders). Without Composite, every operation needs type-checking:

```java
if (item instanceof File) {
    totalSize += ((File) item).getSize();
} else if (item instanceof Folder) {
    for (Object child : ((Folder) item).getChildren()) { /* recurse... */ }
}
```

Composite fixes this — both share the same interface. Call `getSize()` on anything and it just works.

---

## Structure

```
        «interface» FileSystemItem
        getName() | getSize() | print()
                  │
       ┌──────────┴──────────┐
   File (Leaf)          Folder (Composite)
   - name, size         - name, children: List<FileSystemItem>
                        - add() / remove()
                        - getSize() → sums children
                        - print()  → delegates to children
```

| Role          | Class            | Does What                                        |
| ------------- | ---------------- | ------------------------------------------------ |
| **Component** | `FileSystemItem` | Common interface                                 |
| **Leaf**      | `File`           | Returns own size directly                        |
| **Composite** | `Folder`         | Holds children, delegates operations recursively |

---

## Code

```java
// Component
public interface FileSystemItem {
    String getName();
    long getSize();
    void print(String indent);
}

// Leaf
public class File implements FileSystemItem {
    private final String name;
    private final long size;
    public File(String name, long size) { this.name = name; this.size = size; }
    public String getName() { return name; }
    public long getSize() { return size; }
    public void print(String indent) {
        System.out.println(indent + "📄 " + name + " (" + size + " bytes)");
    }
}

// Composite
public class Folder implements FileSystemItem {
    private final String name;
    private final List<FileSystemItem> children = new ArrayList<>();
    public Folder(String name) { this.name = name; }
    public void add(FileSystemItem item) { children.add(item); }
    public void remove(FileSystemItem item) { children.remove(item); }
    public String getName() { return name; }
    public long getSize() {
        long total = 0;
        for (FileSystemItem child : children) total += child.getSize(); // recursive
        return total;
    }
    public void print(String indent) {
        System.out.println(indent + "📁 " + name + " (" + getSize() + " bytes)");
        for (FileSystemItem child : children) child.print(indent + "  ");
    }
}
```

**Client — builds tree, treats everything uniformly:**

```java
Folder root = new Folder("root");
root.add(new File("README.md", 1200));
Folder src = new Folder("src");
src.add(new File("Main.java", 2400));
root.add(src);

root.print("");              // prints entire tree recursively
root.getSize();              // returns total size — no type checks
```

---

## When to Use / Not Use

| Use                                               | Don't Use                                               |
| ------------------------------------------------- | ------------------------------------------------------- |
| Tree/hierarchy of objects                         | Flat structure (no nesting)                             |
| Clients should treat leaves & containers the same | Leaves and containers have fundamentally different APIs |
| Operations need to recurse through the tree       | Fixed, trivial hierarchy                                |

---

## Pitfalls

- **Transparency vs Safety:** Putting `add()`/`remove()` in the Component forces Leaf to throw `UnsupportedOperationException`. Safer to keep them only in Composite.
- **Recursive cost:** `getSize()` visits every node — watch performance on huge trees.
- **Circular refs:** A folder added to itself → stack overflow.

---

## Related Patterns

| Pattern                     | Relationship                                                                   |
| --------------------------- | ------------------------------------------------------------------------------ |
| **Decorator**               | Wraps a _single_ object to add behavior; Composite manages _multiple_ children |
| **Iterator**                | Used _with_ Composite to traverse the tree                                     |
| **Chain of Responsibility** | Can propagate requests along Composite's parent-child links                    |
