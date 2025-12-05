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
        journal.log("CMD: createDirectory " + directoryName);
        Directory newDir = new Directory(directoryName, currentDirectory);
        currentDirectory.addDirectory(newDir);
    }

    public void createFile(String fileName, String content) {
        journal.log("CMD: createFile " + fileName);
        File newFile = new File(fileName);
        newFile.setContent(content);
        currentDirectory.addFile(newFile);
    }

    public void deleteDirectory(String directoryName) {
        journal.log("CMD: deleteDirectory " + directoryName);
        currentDirectory.removeDirectory(directoryName);
    }

    public void deleteFile(String fileName) {
        journal.log("CMD: deleteFile " + fileName);
        currentDirectory.removeFile(fileName);
    }

    public void changeDirectory(String directoryPath) {
        journal.log("CMD: cd " + directoryPath);
        if (directoryPath.equals("..")) {
            if (currentDirectory.getParent() != null) {
                currentDirectory = currentDirectory.getParent();
            } else {
                System.out.println("Já está na raiz.");
            }
        } else {
            Directory target = currentDirectory.getDirectory(directoryPath);
            if (target != null) {
                currentDirectory = target;
            } else {
                System.out.println("Diretório não encontrado: " + directoryPath);
            }
        }
    }

    public void listCurrentDirectory() {
        journal.log("CMD: ls");
        System.out.println("Conteúdo de " + currentDirectory.getName() + ":");
        for (String item : currentDirectory.listContents()) {
            System.out.println(item);
        }
    }

    public void printJournal() {
        System.out.println("--- Log de Journaling ---");
        journal.printLog();
    }

    public String getCurrentPath() {
        return currentDirectory.getName();
    }

}
