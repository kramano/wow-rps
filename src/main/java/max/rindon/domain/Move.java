package max.rindon.domain;

import java.util.EnumMap;

public enum Move {
    ROCK, PAPER, SCISSORS;

    private static final EnumMap<Move, Move> LOSES_TO = new EnumMap<>(Move.class);
    private static final EnumMap<Move, Move> BEATS = new EnumMap<>(Move.class);

    static {
        LOSES_TO.put(ROCK, PAPER);
        LOSES_TO.put(PAPER, SCISSORS);
        LOSES_TO.put(SCISSORS, ROCK);

        BEATS.put(PAPER, ROCK);
        BEATS.put(SCISSORS, PAPER);
        BEATS.put(ROCK, SCISSORS);
    }


    public Move beats() { return BEATS.get(this); }

    public Move losesTo() {
        return LOSES_TO.get(this);
    }

}
