package max.rindon.domain;

import java.util.EnumMap;

import static max.rindon.domain.Move.*;
import static max.rindon.domain.Outcome.*;

public class Rules {
    private static EnumMap<Move, EnumMap<Move, Outcome>> table = new EnumMap<>(Move.class);

    static {
        for (Move move : Move.values()) {
            table.put(move, new EnumMap<>(Move.class));
        }
        initRow(ROCK, DRAW, LOSE, WIN);
        initRow(PAPER, WIN, DRAW, LOSE);
        initRow(SCISSORS, LOSE, WIN, DRAW);
    }

    private static void initRow(Move move, Outcome rock, Outcome paper, Outcome scissors) {
        EnumMap<Move, Outcome> row = table.get(move);
        row.put(ROCK, rock);
        row.put(PAPER, paper);
        row.put(SCISSORS, scissors);
    }

    public static Outcome outcome(Move first, Move second) {
        return table.get(first).get(second);
    }
}
