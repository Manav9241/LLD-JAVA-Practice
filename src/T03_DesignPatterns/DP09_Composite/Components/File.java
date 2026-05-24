package T03_DesignPatterns.DP09_Composite.Components;

public class File implements IFileSystemComponent{
    private String name;
    private int size;

    private String getIndentSpaces(int indent) {
        String indentSpaces = "";
        for (int i = 0; i < indent; i++) {
            indentSpaces += " ";
        }

        return indentSpaces;
    }

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void ls(int indent) {
        String indentSpaces = getIndentSpaces(indent);
        System.out.println(indentSpaces + name);
    }

    @Override
    public void openAll(int indent) {
        String indentSpaces = getIndentSpaces(indent);
        System.out.println(indentSpaces + name);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override 
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public IFileSystemComponent cd(String name) {
        return null;
    }
}
