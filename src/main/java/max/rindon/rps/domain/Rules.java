package max.rindon.rps.domain;

import java.util.EnumMap;

public class Rules {
    private static final EnumMap<Move, EnumMap<Move, Outcome>> OUTCOME_TABLE = new EnumMap<>(Move.class);

    static {
        for (Move move : Move.values()) {
            OUTCOME_TABLE.put(move, new EnumMap<>(Move.class));
        }
        initRow(Move.ROCK, Outcome.DRAW, Outcome.LOSS, Outcome.WIN);
        initRow(Move.PAPER, Outcome.WIN, Outcome.DRAW, Outcome.LOSS);
        initRow(Move.SCISSORS, Outcome.LOSS, Outcome.WIN, Outcome.DRAW);
    }

    private static void initRow(Move move, Outcome rock, Outcome paper, Outcome scissors) {
        EnumMap<Move, Outcome> row = OUTCOME_TABLE.get(move);
        row.put(Move.ROCK, rock);
        row.put(Move.PAPER, paper);
        row.put(Move.SCISSORS, scissors);
    }

    public static Outcome evaluate(Move first, Move second) {
        return OUTCOME_TABLE.get(first).get(second);
    }
}
