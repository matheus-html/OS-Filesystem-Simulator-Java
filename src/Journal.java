import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Journal {
    private List<String> entries;
    private DateTimeFormatter formatter;

    public Journal() {
        this.entries = new ArrayList<>();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public void log(String operation) {
        String timestamp = LocalDateTime.now().format(formatter);
        entries.add("[" + timestamp + "] " + operation);
    }

    public void printLog() {
        for (String entry : entries) {
            System.out.println(entry);
        }
    }
}