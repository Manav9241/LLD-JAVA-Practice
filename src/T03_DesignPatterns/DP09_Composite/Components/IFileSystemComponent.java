package T03_DesignPatterns.DP09_Composite.Components;

public interface IFileSystemComponent {
    void ls(int indent);

    void openAll(int indent);

    int getSize();

    String getName();

    boolean isFolder();

    IFileSystemComponent cd(String name);
}
