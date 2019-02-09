package max.rindon.ai;

import max.rindon.domain.Move;

import java.util.List;
import java.util.Random;

public class Strategies {
    public static final Strategy ALWAYS_ROCK = always(Move.ROCK);
    public static final Strategy ALWAYS_PAPER = always(Move.PAPER);
    public static final Strategy ALWAYS_SCISSORS = always(Move.SCISSORS);

    public static Strategy always(Move move) {
        return (noMatterWhat -> move);
    }

    public static Strategy random() {
        return new RandomStrategy();
    }

    private static class RandomStrategy implements Strategy {

        private final Random random = new Random();

        @Override
        public Move apply(List<Move> moves) {
            return Move.values()[random.nextInt(Move.values().length)];
        }
    }

}
