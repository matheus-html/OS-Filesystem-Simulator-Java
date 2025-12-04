import java.util.ArrayList;
import java.util.List;

public class Directory {
    private String name;
    private List<File> files;
    private List<Directory> directories;
    private Directory parent;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
        this.files = new ArrayList<>();
        this.directories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public void removeFile(String filename) {
        this.files.remove(new File(filename));
    }

    public File  getFile(String filename) {
        return null;
    }

    public void addDirectory(Directory directory) {
        this.directories.add(directory);
    }

    public void removeDirectory(Directory directory) {
        this.directories.remove(directory);
    }

    public Directory getDirectory(String directory) {
        return null;
    }

    public List<String> listContents() {
        return null;
    }
}
