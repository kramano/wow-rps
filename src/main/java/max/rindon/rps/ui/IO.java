package max.rindon.rps.ui;

public interface IO {

    String read();

    void write(String message);

    default String prompt(String message) {
        write(message);
        return read();
    }
}
