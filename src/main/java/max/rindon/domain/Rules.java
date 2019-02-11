package max.rindon.domain;

import java.util.EnumMap;

import static max.rindon.domain.Move.*;
import static max.rindon.domain.Outcome.*;

public class Rules {
    private static final EnumMap<Move, EnumMap<Move, Outcome>> OUTCOME_TABLE = new EnumMap<>(Move.class);
    private static final EnumMap<Move, Move> LOSES_TO = new EnumMap<>(Move.class);

    static {
        for (Move move : Move.values()) {
            OUTCOME_TABLE.put(move, new EnumMap<>(Move.class));
        }
        initRow(ROCK, DRAW, LOSS, WIN);
        initRow(PAPER, WIN, DRAW, LOSS);
        initRow(SCISSORS, LOSS, WIN, DRAW);

        LOSES_TO.put(ROCK, PAPER);
        LOSES_TO.put(PAPER, SCISSORS);
        LOSES_TO.put(SCISSORS, ROCK);
    }

    private static void initRow(Move move, Outcome rock, Outcome paper, Outcome scissors) {
        EnumMap<Move, Outcome> row = OUTCOME_TABLE.get(move);
        row.put(ROCK, rock);
        row.put(PAPER, paper);
        row.put(SCISSORS, scissors);
    }

    public static Outcome evaluate(Move first, Move second) {
        return OUTCOME_TABLE.get(first).get(second);
    }

    public static Move losesTo(Move move) {
        return LOSES_TO.get(move);
    }
}
