package T03_DesignPatterns.DP09_Composite;

import T03_DesignPatterns.DP09_Composite.Components.File;
import T03_DesignPatterns.DP09_Composite.Components.Folder;
import T03_DesignPatterns.DP09_Composite.Components.IFileSystemComponent;

public class CompositePatternMain {
    public static void main(String[] args) {
        Folder root = new Folder("root");
        root.add(new File("file1.txt", 1));
        root.add(new File("file2.txt", 2));

        Folder docs = new Folder("docs");
        docs.add(new File("resume.pdf", 5));
        docs.add(new File("notes.txt", 3));
        root.add(docs);

        Folder images = new Folder("images");
        images.add(new File("photo.jpg", 8));
        root.add(images);

        root.ls(0);

        docs.ls(0);

        root.openAll(0);

        IFileSystemComponent cwd = root.cd("docs");
        if (cwd != null) {
            cwd.ls(0);
        } else {
            System.out.println("\nCould not cd\n");
        }

        System.out.println(root.getSize());
    }
}
