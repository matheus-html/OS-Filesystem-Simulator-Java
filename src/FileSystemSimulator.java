import java.io.*;
import java.util.Scanner;

public class FileSystemSimulator {
    private Directory root;
    private Directory currentDirectory;
    private Journal journal;
    private static final String DATA_FILE = "filesystem.dat";

    public FileSystemSimulator() {
        this.journal = new Journal();

        if (!loadFileSystem()) {
            this.root = new Directory("/", null);
            this.currentDirectory = this.root;
            journal.log("SYSTEM: Novo sistema de arquivos inicializado.");
            saveFileSystem();
        } else {
            journal.log("SYSTEM: Sistema de arquivos restaurado de " + DATA_FILE);
        }
    }

    private boolean loadFileSystem() {
        java.io.File dataFile = new java.io.File(DATA_FILE);

        if (!dataFile.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            this.root = (Directory) ois.readObject();
            this.currentDirectory = this.root;
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar sistema de arquivos: " + e.getMessage());
            return false;
        }
    }

    public void saveFileSystem() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this.root);
        } catch (IOException e) {
            System.out.println("Erro crítico ao salvar sistema de arquivos: " + e.getMessage());
        }
    }

    public void createDirectory(String directoryName) {
        journal.log("CMD: MKDIR " + directoryName);
        Directory newDir = new Directory(directoryName, currentDirectory);
        currentDirectory.addDirectory(newDir);
        saveFileSystem();
    }

    public void createFile(String fileName, String content) {
        journal.log("CMD: TOUCH " + fileName);
        File newFile = new File(fileName);
        newFile.setContent(content);
        currentDirectory.addFile(newFile);
        saveFileSystem();
    }

    public void deleteDirectory(String directoryName) {
        journal.log("CMD: RMDIR " + directoryName);
        currentDirectory.removeDirectory(directoryName);
        saveFileSystem();
    }

    public void deleteFile(String fileName) {
        journal.log("CMD: DEL " + fileName);
        currentDirectory.removeFile(fileName);
        saveFileSystem();
    }

    public void rename(String oldName, String newName) {
        journal.log("CMD: REN " + oldName + " " + newName);

        if (currentDirectory.getFile(newName) != null || currentDirectory.getDirectory(newName) != null) {
            System.out.println("Erro: Já existe um arquivo ou diretório com o nome '" + newName + "'.");
            return;
        }

        File fileToRename = currentDirectory.getFile(oldName);
        if (fileToRename != null) {
            fileToRename.setFilename(newName);
            System.out.println("Arquivo renomeado com sucesso.");
            saveFileSystem();
            return;
        }

        Directory dirToRename = currentDirectory.getDirectory(oldName);
        if (dirToRename != null) {
            dirToRename.setName(newName);
            System.out.println("Diretório renomeado com sucesso.");
            saveFileSystem();
            return;
        }

        System.out.println("Erro: O arquivo ou diretório '" + oldName + "' não foi encontrado.");
    }

    public void copy(String sourceName, String destName) {
        File sourceFile = currentDirectory.getFile(sourceName);

        if (sourceFile == null) {
            System.out.println("Erro: O arquivo de origem '" + sourceName + "' não existe.");
            return;
        }

        Directory targetDir = currentDirectory.getDirectory(destName);
        if (targetDir != null) {
            journal.log("CMD: COPY " + sourceName + " TO DIR " + destName);

            File newFile = new File(sourceFile.getFilename());
            newFile.setContent(sourceFile.getContent());

            targetDir.addFile(newFile);
            System.out.println("Arquivo copiado para dentro de '" + destName + "'.");
            saveFileSystem();
            return;
        }

        if (currentDirectory.getFile(destName) != null) {
            System.out.println("Erro: Já existe um arquivo com o nome '" + destName + "'.");
            return;
        }

        journal.log("CMD: COPY " + sourceName + " AS " + destName);
        File newFile = new File(destName);
        newFile.setContent(sourceFile.getContent());

        currentDirectory.addFile(newFile);
        System.out.println("Arquivo duplicado como '" + destName + "'.");
        saveFileSystem();
    }

    public void changeDirectory(String directoryPath) {
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
        System.out.println("Conteúdo de " + currentDirectory.getName() + ":");
        for (String item : currentDirectory.listContents()) {
            System.out.println(item);
        }
    }

    public void printJournal() {
        System.out.println("--- Log de Journaling (Leitura do disco) ---");
        journal.printLog();
    }

    public String getCurrentPath() {
        return currentDirectory.getName();
    }

    public static void main(String[] args) {
        FileSystemSimulator fs = new FileSystemSimulator();
        Scanner scanner = new Scanner(System.in);
        String command = "";

        System.out.println("Simulador Iniciado. Digite 'help' para comandos.");

        while (!command.equals("exit")) {
            System.out.print(fs.getCurrentPath() + "> ");
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) continue;

            String[] parts = input.split(" ");
            command = parts[0];

            switch (command) {
                case "mkdir":
                    if (parts.length > 1) fs.createDirectory(parts[1]);
                    else System.out.println("Sintaxe: mkdir <nome>");
                    break;
                case "touch":
                    if (parts.length > 1) fs.createFile(parts[1], "Vazio");
                    else System.out.println("Sintaxe: touch <nome>");
                    break;
                case "rmdir":
                    if (parts.length > 1) fs.deleteDirectory(parts[1]);
                    else System.out.println("Sintaxe: rmdir <nome>");
                    break;
                case "del":
                    if (parts.length > 1) fs.deleteFile(parts[1]);
                    else System.out.println("Sintaxe: del <nome>");
                    break;
                case "ren":
                    if (parts.length > 2) fs.rename(parts[1], parts[2]);
                    else System.out.println("Sintaxe: ren <antigo> <novo>");
                    break;
                case "copy":
                    if (parts.length > 2) fs.copy(parts[1], parts[2]);
                    else System.out.println("Sintaxe: copy <origem> <destino>");
                    break;
                case "cd":
                    if (parts.length > 1) fs.changeDirectory(parts[1]);
                    else System.out.println("Sintaxe: cd <nome> ou cd ..");
                    break;
                case "dir":
                    fs.listCurrentDirectory();
                    break;
                case "journal":
                    fs.printJournal();
                    break;
                case "help":
                    System.out.println("\n" +
                            "CD <dir>       Exibe o nome do diretório atual ou faz alterações nele.\n" +
                            "COPY <or> <ds> Copia um arquivo para outro nome ou para dentro de um diretório.\n" +
                            "DEL <arq>      Exclui um ou mais arquivos.\n" +
                            "DIR            Exibe uma lista de arquivos e subdiretórios em um diretório.\n" +
                            "EXIT           Sai do programa interpretador de comandos.\n" +
                            "JOURNAL        Exibe o histórico de logs do sistema de arquivos.\n" +
                            "MKDIR <dir>    Cria um diretório.\n" +
                            "REN <ant> <nv> Renomeia um arquivo ou diretório.\n" +
                            "RMDIR <dir>    Remove um diretório.\n" +
                            "TOUCH <arq>    Cria um arquivo de texto novo (Simulado).\n");
                    break;
                case "exit":
                    fs.saveFileSystem();
                    System.out.println("Encerrando simulador.");
                    break;
                default:
                    System.out.println("Comando não reconhecido.");
            }
        }
        scanner.close();
    }
}