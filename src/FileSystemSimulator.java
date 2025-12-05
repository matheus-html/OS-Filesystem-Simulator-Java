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
                    else System.out.println("Erro: Forneça o nome do diretório.");
                    break;
                case "touch":
                    if (parts.length > 1) fs.createFile(parts[1], "Vazio");
                    else System.out.println("Erro: Forneça o nome do arquivo.");
                    break;
                case "rmdir":
                    if (parts.length > 1) fs.deleteDirectory(parts[1]);
                    else System.out.println("Erro: Forneça o nome do diretório.");
                    break;
                case "rm":
                    if (parts.length > 1) fs.deleteFile(parts[1]);
                    else System.out.println("Erro: Forneça o nome do arquivo.");
                    break;
                case "cd":
                    if (parts.length > 1) fs.changeDirectory(parts[1]);
                    else System.out.println("Erro: Forneça o caminho.");
                    break;
                case "ls":
                    fs.listCurrentDirectory();
                    break;
                case "journal":
                    fs.printJournal();
                    break;
                case "help":
                    System.out.println("\n--- Comandos Disponíveis ---" +
                            "\n mkdir [nome]   -> Cria um novo diretório" +
                            "\n touch [nome]   -> Cria um novo arquivo vazio" +
                            "\n rm [nome]      -> Remove um arquivo" +
                            "\n rmdir [nome]   -> Remove um diretório" +
                            "\n cd [nome]      -> Entra num diretório (use '..' para voltar)" +
                            "\n ls             -> Lista o conteúdo do diretório atual" +
                            "\n journal        -> Exibe o histórico de operações (Log)" +
                            "\n exit           -> Encerra o simulador\n");
                    break;
                case "exit":
                    System.out.println("Encerrando simulador.");
                    break;
                default:
                    System.out.println("Comando desconhecido.");
            }
        }
        scanner.close();
    }
}
