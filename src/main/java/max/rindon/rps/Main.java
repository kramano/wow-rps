package max.rindon.rps;

import max.rindon.rps.ai.Strategies;
import max.rindon.rps.ai.Strategy;
import max.rindon.rps.domain.RockPaperScissors;
import max.rindon.rps.ui.Command;
import max.rindon.rps.ui.UI;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Strategy aiStrategy = Strategies.markovChain(Strategies.random());

        RockPaperScissors game = new RockPaperScissors(aiStrategy);

        show(UI.WELCOME_MESSAGE);
        show(UI.HELP_MESSAGE);

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                show(UI.MOVE_PROMPT_MESSAGE);
                String playerInput = sc.nextLine();
                // check for command
                Command command = UI.parseCommand(playerInput).orElse(Command.CONTINUE);

                if (command == Command.QUIT) {
                    show(UI.statistics2Message(game.getStatistics()));
                    return;
                } else if (command == Command.HELP) {
                    show(UI.HELP_MESSAGE);
                    continue;
                } else if (command == Command.STATS) {
                    show(UI.statistics2Message(game.getStatistics()));
                    continue;
                }

                String message = UI
                        .parseMove(playerInput)
                        .map(game::playRound)
                        .map(UI::round2Message)
                        .orElse(UI.parseMoveError(playerInput));

                show(message);
            }
        }
    }

    private static void show(String message) {
        System.out.println(message);
    }
}
