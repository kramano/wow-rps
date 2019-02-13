package max.rindon.rps;

import max.rindon.rps.ai.Strategies;
import max.rindon.rps.ai.Strategy;
import max.rindon.rps.ui.ConsoleIO;

import java.io.PrintStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Strategy aiStrategy = Strategies.markovChain(Strategies.random());

        try (Scanner sc = new Scanner(System.in); PrintStream ps = System.out) {
            ConsoleIO io = new ConsoleIO(sc, ps);
            GameRunner gm = new GameRunner(io, aiStrategy);
            gm.run();
        }
    }
}

