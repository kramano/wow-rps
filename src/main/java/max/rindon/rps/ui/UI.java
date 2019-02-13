package max.rindon.rps.ui;

import max.rindon.rps.domain.Move;
import max.rindon.rps.domain.Outcome;
import max.rindon.rps.domain.Round;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static max.rindon.rps.util.Maps.entriesToMap;
import static max.rindon.rps.util.Maps.entry;

public final class UI {

    private static final String ROCK_ART =
            "    _______\n" +
            "---'   ____)\n" +
            "      (_____)\n" +
            "      (_____)\n" +
            "      (____)\n" +
            "---.__(___)\n";

    private static final String PAPER_ART =
            "    _______\n" +
            "---'   ____)____\n" +
            "          ______)\n" +
            "          _______)\n" +
            "         _______)\n" +
            "---.__________)\n";

    private static final String SCISSORS_ART =
            "    _______\n" +
            "---'   ____)____\n" +
            "          ______)\n" +
            "       __________)\n" +
            "      (____)\n" +
            "---.__(___)";

    public static final String PARSE_MOVE_MESSAGE = "Sorry, I didn't understand your move: ";
    public static final String WELCOME_MESSAGE = "Welcome to the game of Rock, Paper and Scissors!";
    public static final String MOVE_PROMPT_MESSAGE = "Please choose one of: (r)ock, (p)aper, (s)cissors";

    private static final String SEP = System.getProperty("line.separator");

    // could handle input string case in parse function to save some typing,
    // but we want to be explicit about the input we accept
    private static final Map<String, Move> STRING_TO_MOVE = unmodifiableMap(Stream.of(
            entry("s", Move.SCISSORS),
            entry("S", Move.SCISSORS),
            entry("r", Move.ROCK),
            entry("R", Move.ROCK),
            entry("p", Move.PAPER),
            entry("P", Move.PAPER))
            .collect(entriesToMap()));

    private static final Map<String, Command> STRING_TO_COMMAND = unmodifiableMap(Stream.of(
            entry(":q", Command.QUIT),
            entry(":h", Command.HELP),
            entry(":s", Command.STATS))
            .collect(entriesToMap()));

    private static final Map<Outcome, String> OUTCOME_TO_MESSAGE = unmodifiableMap(Stream.of(
            entry(Outcome.WIN, "Greetings, you won!"),
            entry(Outcome.LOSS, "Sorry, you lost. Maybe next time!"),
            entry(Outcome.DRAW, "And it's a draw."))
            .collect(entriesToMap()));

    private static final Map<Move, String> MOVE_TO_ART = unmodifiableMap(Stream.of(
            entry(Move.ROCK, ROCK_ART),
            entry(Move.PAPER, PAPER_ART),
            entry(Move.SCISSORS, SCISSORS_ART))
            .collect(entriesToMap()));

    public static final String HELP_MESSAGE = String.join(SEP,
            ":q - quit game",
            ":h - show help",
            ":s - show game statistics");

    public static Optional<Move> parseMove(String moveString) {
        return Optional.ofNullable(STRING_TO_MOVE.get(moveString));
    }

    public static Optional<Command> parseCommand(String commandString) {
        return Optional.ofNullable(STRING_TO_COMMAND.get(commandString));
    }

    public static String outcome2Message(Outcome outcome) {
        return OUTCOME_TO_MESSAGE.get(outcome);
    }

    public static String renderRound(Round round) {
        return String.join(SEP,
                MOVE_TO_ART.get(round.playerMove),
                ">=====VS=====<",
                MOVE_TO_ART.get(round.aiMove),
                outcome2Message(round.outcome),
                "-------------------------------------------------------");
    }

    public static String renderStatistics(Map<Outcome, Integer> statistics) {
        return String.join(SEP,
                "You: " + statistics.get(Outcome.WIN),
                           "AI: " + statistics.get(Outcome.LOSS),
                           "Draw: " + statistics.get(Outcome.DRAW));
    }

    public static String renderParseMoveError(String playerInput) {
        return PARSE_MOVE_MESSAGE + playerInput;
    }
}
