package T03_DesignPatterns.DP09_Composite.Components;

import java.util.ArrayList;
import java.util.List;

public class Folder implements IFileSystemComponent {
    private String name;
    private List<IFileSystemComponent> children;

    private String getIndentSpaces(int indent) {
        String indentSpaces = "";
        for (int i = 0; i < indent; i++) {
            indentSpaces += " ";
        }

        return indentSpaces;
    }

    public Folder(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void add(IFileSystemComponent child) {
        children.add(child);
    }

    @Override
    public void ls(int indent) {
        String indentSpace = getIndentSpaces(indent);
        for (IFileSystemComponent child : children) {
            if (child.isFolder()) {
                System.out.println(indentSpace + "+ " + child.getName());
            } else {
                System.out.println(indentSpace + child.getName());
            }
        }
    }

    @Override
    public void openAll(int indent) {
        String indentSpaces = getIndentSpaces(indent);
        System.out.println(indentSpaces + "+ " + name);
        for (IFileSystemComponent child : children) {
            child.openAll(indent + 4);
        }
    }

    @Override
    public int getSize() {
        int total = 0;
        for (IFileSystemComponent child : children) {
            total += child.getSize();
        }
        return total;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IFileSystemComponent cd(String target) {
        for (IFileSystemComponent child : children) {
            if (child.isFolder() && child.getName().equalsIgnoreCase(target)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public boolean isFolder() {
        return true;
    }
}
