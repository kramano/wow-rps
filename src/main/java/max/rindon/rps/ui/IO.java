package max.rindon.rps.ui;

public interface IO {

    String read();

    void write(String message);

    String prompt(String message);
}
