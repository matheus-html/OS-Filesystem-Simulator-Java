import java.util.Scanner;

public class FileSystemSimulator {
    private Directory root;
    private Directory currentDirectory;
    private Journal journal;

    public FileSystemSimulator() {
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

    public static void main(String[] args) {
        FileSystemSimulator fs = new FileSystemSimulator();
        Scanner scanner = new Scanner(System.in);
        String command = "";

        System.out.println("Simulador de Sistema de Arquivos Iniciado. Digite 'help' para comandos.");

        while (!command.equals("exit")) {
            System.out.print(fs.getCurrentPath() + "> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            command = parts[0];

            switch (command) {
                case "mkdir":
                    if (parts.length > 1) fs.createDirectory(parts[1]);
                    else System.out.println("A sintaxe do comando está incorreta.");
                    break;
                case "touch":
                    if (parts.length > 1) fs.createFile(parts[1], "Vazio");
                    else System.out.println("A sintaxe do comando está incorreta.");
                    break;
                case "rmdir":
                    if (parts.length > 1) fs.deleteDirectory(parts[1]);
                    else System.out.println("A sintaxe do comando está incorreta.");
                    break;
                case "del": // Antigo rm
                    if (parts.length > 1) fs.deleteFile(parts[1]);
                    else System.out.println("A sintaxe do comando está incorreta.");
                    break;
                case "cd":
                    if (parts.length > 1) fs.changeDirectory(parts[1]);
                    else System.out.println("A sintaxe do comando está incorreta.");
                    break;
                case "dir":
                    fs.listCurrentDirectory();
                    break;
                case "journal":
                    fs.printJournal();
                    break;
                case "help":
                    System.out.println("\n" +
                            "CD             Exibe o nome do diretório atual ou faz alterações nele.\n" +
                            "DEL            Exclui um ou mais arquivos.\n" +
                            "DIR            Exibe uma lista de arquivos e subdiretórios em um diretório.\n" +
                            "EXIT           Sai do programa interpretador de comandos.\n" +
                            "JOURNAL        Exibe o histórico de logs do sistema de arquivos.\n" +
                            "MKDIR          Cria um diretório.\n" +
                            "RMDIR          Remove um diretório.\n" +
                            "TOUCH          Cria um arquivo de texto novo (Simulado).\n");
                    break;
                case "exit":
                    System.out.println("Encerrando simulador.");
                    break;
                default:
                    System.out.println("'" + command + "' não é reconhecido como um comando interno\n" +
                            "ou externo, um programa operável ou um arquivo em lotes.");
            }
        }
        scanner.close();
    }
}
