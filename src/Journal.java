import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Journal {
    private static final String LOG_FILE = "journal.txt";
    private DateTimeFormatter formatter;

    public Journal() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public void log(String operation) {
        String timestamp = LocalDateTime.now().format(formatter);
        String entry = "[" + timestamp + "] " + operation;

        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(entry);
        } catch (IOException e) {
            System.out.println("Erro ao escrever no journal: " + e.getMessage());
        }
    }

    public void printLog() {
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Nenhum log encontrado ou erro de leitura.");
        }
    }
}