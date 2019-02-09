package max.rindon.temp;

import max.rindon.ai.Strategies;
import max.rindon.domain.Move;
import max.rindon.domain.Outcome;
import max.rindon.domain.Round;
import max.rindon.domain.Rules;
import max.rindon.ui.Command;
import max.rindon.ui.UI;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Round> gameLog = new ArrayList<>();
        // Show welcome message
        System.out.println(UI.WELCOME_MESSAGE);
        // show help message
        System.out.println(UI.HELP_MESSAGE);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(UI.MOVE_PROMPT_MESSAGE);
            String playerInput = sc.nextLine();
            // check for command
            Command command = UI.parseCommand(playerInput).orElse(Command.CONTINUE);

            if (command == Command.QUIT) {
                // display stats
                System.out.println(computesStatistics(gameLog));
                return;
            } else if (command == Command.HELP) {
                // display help message
                System.out.println(UI.HELP_MESSAGE);
                continue;
            } else if (command == Command.STATS) {
                // display stats
                System.out.println(computesStatistics(gameLog));
                continue;
            }

            Move aiMove = Strategies.random().apply(Collections.emptyList());
            String message = UI
                    .parseMove(playerInput)
                    .map(playerMove -> playRound(playerMove, aiMove, gameLog))
                    .map(UI::displayRoundResults)
                    .orElse("Sorry, I don't understand your move: " + playerInput);

            System.out.println(message);
        }
    }

    // TODO: refactor gameLog mutation inside the method
    private static Round playRound(Move playerMove, Move aiMove, List<Round> gameLog) {
        Outcome outcome = Rules.outcome(playerMove, aiMove);
        Round round = new Round(playerMove, aiMove, outcome);
        gameLog.add(round);
        return round;
    }

    private static Stats computesStatistics(List<Round> log) {
        long totalPlayed = log.size();
        Map<Outcome, Long> stats = log.stream()
                .collect(Collectors
                        .groupingBy(r -> r.outcome, Collectors.counting()));

        return new Stats(totalPlayed,
                stats.getOrDefault(Outcome.WIN, 0L),
                stats.getOrDefault(Outcome.LOSE, 0L),
                stats.getOrDefault(Outcome.DRAW, 0L));
    }

    private static class Stats {
        final long totalRoundsPlayed;
        final long playerWins;
        final long aiWins;
        final long draws;

        public Stats(long totalRoundsPlayed, long playerWins, long aiWins, long draws) {
            this.totalRoundsPlayed = totalRoundsPlayed;
            this.playerWins = playerWins;
            this.aiWins = aiWins;
            this.draws = draws;
        }

        @Override
        public String toString() {
            return "Stats{" +
                    "totalRoundsPlayed=" + totalRoundsPlayed +
                    ", playerWins=" + playerWins +
                    ", aiWins=" + aiWins +
                    ", draws=" + draws +
                    '}';
        }
    }
}
