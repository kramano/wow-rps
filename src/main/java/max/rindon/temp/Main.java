package max.rindon.temp;

import max.rindon.ai.Strategies;
import max.rindon.ai.Strategy;
import max.rindon.domain.Move;
import max.rindon.domain.RockPaperScissors;
import max.rindon.ui.Command;
import max.rindon.ui.UI;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Strategy aiStrategy = Strategies.firstThenOther(Strategies.random(),
                Strategies.beatMostFrequent(Move.ROCK));

        RockPaperScissors game = new RockPaperScissors(aiStrategy);

        // Show welcome message
        System.out.println(UI.WELCOME_MESSAGE);
        // show help message
        System.out.println(UI.HELP_MESSAGE);

        Scanner sc = new Scanner(System.in); // TODO: close it
        while (true) {
            System.out.println(UI.MOVE_PROMPT_MESSAGE);
            String playerInput = sc.nextLine();
            // check for command
            Command command = UI.parseCommand(playerInput).orElse(Command.CONTINUE);

            if (command == Command.QUIT) {
                // display stats
                System.out.println(UI.statistics2Message(game.getStatistics()));
                return;
            } else if (command == Command.HELP) {
                // display help message
                System.out.println(UI.HELP_MESSAGE);
                continue;
            } else if (command == Command.STATS) {
                // display stats
                System.out.println(UI.statistics2Message(game.getStatistics()));
                continue;
            }

            String message = UI
                    .parseMove(playerInput)
                    .map(game::playRound)
                    .map(UI::round2Message)
                    .orElse("Sorry, I didn't understand your move: " + playerInput);

            System.out.println(message);
        }
    }
}
