public class FileSystemSimulator {
    private Directory root;
    private Directory currentDirectory;
    private Journal journal;

    public FileSystemSimulator(Directory root, Journal journal) {
        this.root = new Directory("/", null);
        this.currentDirectory = this.root;
        this.journal = new Journal();
    }

    public void createDirectory(String directoryName) {

    }

    public void createFile(String fileName) {

    }

    public void deleteDirectory(String directoryName) {

    }

    public void deleteFile(String fileName) {

    }

    public void changeDirectory(String directoryName) {

    }

    public void listCurrentDirectory(String directoryName) {

    }

    public void printJournal() {

    }

}
