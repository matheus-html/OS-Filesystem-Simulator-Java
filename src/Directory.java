import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Directory implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public void setName(String name) {
        this.name = name;
    }

    public Directory getParent() {
        return parent;
    }

    public void addFile(File file) {
        if (getFile(file.getFilename()) == null) {
            this.files.add(file);
        } else {
            System.out.println("Um arquivo já existe com esse nome.");
        }
    }

    public void removeFile(String filename) {
        File removeFile = getFile(filename);
        if (removeFile != null) {
            files.remove(removeFile);
        }
    }

    public File getFile(String filename) {
        for (File file : files) {
            if (file.getFilename().equals(filename)) {
                return file;
            }
        }
        return null;
    }

    public void addDirectory(Directory directory) {
        if (getDirectory(directory.getName()) == null) {
            this.directories.add(directory);
        } else {
            System.out.println("Um diretório já existe com esse nome.");
        }
    }

    public void removeDirectory(String directoryName) {
        Directory dirToRemove = getDirectory(directoryName);
        if (dirToRemove != null) {
            directories.remove(dirToRemove);
        }
    }

    public Directory getDirectory(String directoryName) {
        for (Directory directory : directories) {
            if (directory.getName().equals(directoryName)) {
                return directory;
            }
        }
        return null;
    }

    public List<String> listContents() {
        List<String> contents = new ArrayList<>();
        for (Directory dir : directories) {
            contents.add("[DIR] " + dir.getName());
        }
        for (File file : files) {
            contents.add("[FILE] " + file.getFilename());
        }
        return contents;
    }
}