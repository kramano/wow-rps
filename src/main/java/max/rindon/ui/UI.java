package max.rindon.ui;

import max.rindon.domain.Move;
import max.rindon.domain.Outcome;
import max.rindon.domain.Round;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;
import static max.rindon.domain.Move.*;
import static max.rindon.domain.Outcome.*;
import static max.rindon.util.Maps.entriesToMap;
import static max.rindon.util.Maps.entry;

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

    public static final String WELCOME_MESSAGE = "Welcome to the game of Rock, Paper and Scissors!";
    public static final String MOVE_PROMPT_MESSAGE = "Please choose one of: (Rr)ock, (Pp)aper, (Ss)cissors";

    private static final String SEP = System.getProperty("line.separator");

    // could handle input string case in parse function to save some typing,
    // but we want to be explicit about the input we accept
    private static final Map<String, Move> STRING_TO_MOVE = unmodifiableMap(Stream.of(
            entry("s", SCISSORS),
            entry("S", SCISSORS),
            entry("r", ROCK),
            entry("R", ROCK),
            entry("p", PAPER),
            entry("P", PAPER))
            .collect(entriesToMap()));

    private static final Map<String, Command> STRING_TO_COMMAND = unmodifiableMap(Stream.of(
            entry(":q", Command.QUIT),
            entry(":h", Command.HELP),
            entry(":s", Command.STATS))
            .collect(entriesToMap()));

    private static final Map<Outcome, String> OUTCOME_TO_MESSAGE = unmodifiableMap(Stream.of(
            entry(WIN, "Greetings, you won!"),
            entry(LOSS, "Sorry, you lost. Maybe next time!"),
            entry(DRAW, "And it's a draw."))
            .collect(entriesToMap()));

    private static final Map<Move, String> MOVE_TO_ART = unmodifiableMap(Stream.of(
            entry(ROCK, ROCK_ART),
            entry(PAPER, PAPER_ART),
            entry(SCISSORS, SCISSORS_ART))
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

    public static String round2Message(Round round) {
        return String.join(SEP,
                MOVE_TO_ART.get(round.playerMove),
                ">=====VS=====<",
                MOVE_TO_ART.get(round.aiMove),
                outcome2Message(round.outcome),
                "-------------------------------------------------------");
    }

    public static String statistics2Message(EnumMap<Outcome, Integer> statistics) {
        return String.join(SEP,
                "You: " + statistics.get(WIN),
                           "AI: " + statistics.get(LOSS),
                           "Draw: " + statistics.get(DRAW));
    }
}
