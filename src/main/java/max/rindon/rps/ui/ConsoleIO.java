package max.rindon.rps.ui;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIO implements IO {

    private final Scanner in;
    private final PrintStream out;

    public ConsoleIO(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public String read() {
        return in.nextLine();
    }

    @Override
    public void write(String message) {
        out.println(message);
    }

    @Override
    public String prompt(String message) {
        write(message);
        return read();
    }
}
