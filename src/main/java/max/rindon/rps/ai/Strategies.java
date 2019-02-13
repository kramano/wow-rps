package max.rindon.rps.ai;

import max.rindon.rps.domain.Move;
import max.rindon.rps.util.Streams;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

/**
 * Static factories and some combinators for strategies.
 * Combinators allow to create more complex strategies from simpler ones.
 * For example, {@link Strategies#random} strategy
 * could be implemented in terms of constant strategies using {@link Strategies#toss(Strategy, Strategy) } combinator.
 * All public Strategies requiring history are exposed using {@link Strategies#firstThenOther(Strategy, Strategy)} combinator.
 */
public class Strategies {

    private Strategies() { }

    private static final Random RANDOM = new Random();

    // some dumb strategies for testing
    public static final Strategy ALWAYS_ROCK = always(Move.ROCK);
    public static final Strategy ALWAYS_PAPER = always(Move.PAPER);
    public static final Strategy ALWAYS_SCISSORS = always(Move.SCISSORS);

    /**
     * Always make given move.
     *
     * @param move Move to make each time.
     * @return Strategy always makes given move.
     */
    public static Strategy always(Move move) {
        return (noMatterWhat -> move);
    }

    /**
     * Make a random move. It is impossible to gain an advantage over a truly random opponent.
     * @param gen Random number generator
     * @return Strategy that makes random moves.
     */
    public static Strategy random(Function<Integer, Integer> gen) {
        return (ignored -> randomMove(gen));
    }

    /**
     * Make a random move. It is impossible to gain an advantage over a truly random opponent.
     * @return Strategy that makes random moves.
     */
    public static Strategy random() {
        return random(RANDOM::nextInt);
    }

    private static Strategy echo() {
        return (moves -> moves.get(moves.size() - 1));
    }

    /**
     * Echo the previous move; We have to supply default Move
     * @param onFirst strategy to use on first turn
     *
     * @return Strategy that repeats last opponent move
     */
    public static Strategy echo(Strategy onFirst) {
        return firstThenOther(onFirst, echo());
    }


    private static Strategy lastLost() {
        return (moves -> getLast(moves).beats());
    }

    /**
     * Return a move that would have lost the last play
     *
     * @param onFirst strategy to use on first turn
     * @return strategy that returns a move that would have lost the last play
     */
    public static Strategy lastLost(Strategy onFirst) {
        return firstThenOther(onFirst, lastLost());
    }

    private static Strategy beatMostFrequent() {
        return (moves -> moves.stream()
                .collect(Collectors.groupingBy(identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .map(Move::losesTo)
                .orElseThrow(() -> new IllegalStateException("Unable to find most frequent move."))); // should never happen
    }

    /**
     * Beats most frequent opponent move
     *
     * @param onFirst strategy to use on first turn
     * @return strategy that returns a move that beats most frequent opponent move
     */
    public static Strategy beatMostFrequent(Strategy onFirst) {
        return firstThenOther(onFirst, beatMostFrequent());
    }


    /**
     * @see MarkovChainStrategy
     * @param onFirst strategy to use on first turn
     * @return simple Markov chain strategy
     */
    public static Strategy markovChain(Strategy onFirst, Function<Integer, Integer> gen) {
        return firstThenOther(onFirst, new MarkovChainStrategy(gen));
    }

    /**
     * @see MarkovChainStrategy
     * @param onFirst strategy to use on first turn
     * @return simple Markov chain strategy
     */
    public static Strategy markovChain(Strategy onFirst) {
        return firstThenOther(onFirst, new MarkovChainStrategy(RANDOM::nextInt));
    }

    /**
     * Make a random choice of which Strategy to use each turn
     * @param first strategy
     * @param second strategy
     * @param gen random number generator
     * @return Strategy making a random choice of which strategy to use each turn
     */
    public static Strategy toss(Strategy first, Strategy second, Function<Integer, Integer> gen) {
        int rand = gen.apply(2);
        return (moves -> {
            if (rand == 0) {
                return first.makeMove(moves);
            } else {
                return second.makeMove(moves);
            }
        });
    }

    /**
     * Make a random choice of which Strategy to use each turn
     * @param first strategy
     * @param second strategy
     * @return Strategy making a random choice of which strategy to use each turn
     */
    public static Strategy toss(Strategy first, Strategy second) {
        return toss(first, second, RANDOM::nextInt);
    }

    /**
     * Alternate strategies each turn
     * @param onEven strategy to use on even turns
     * @param onOdd strategy to use on odd turns
     * @return strategy alternating between given strategies
     */
    public static Strategy alternate(Strategy onEven, Strategy onOdd) {
        return (moves -> {
            if (moves.size() % 2 == 0) {
                return onEven.makeMove(moves);
            } else {
                return onOdd.makeMove(moves);
            }
        });
    }

    /**
     * Use first strategy if this is first turn. Otherwise use the other strategy
     * This may be useful when other strategy requires history
     * @param onFirst strategy to use on first turn
     * @param onRemaining strategy to use for remaining turns
     * @return strategy using one strategy on first turn and other for remaining turns
     */
    public static Strategy firstThenOther(Strategy onFirst, Strategy onRemaining) {
        return (moves -> {
            if (moves.size() == 0) {
                return onFirst.makeMove(moves);
            } else {
                return onRemaining.makeMove(moves);
            }
        });
    }

    /**
     * Simple strategy based on Markov Chains.
     * <p> Our main weapon in fighting humans ;)
     * <p> We are building a map (Move -> [Move]) where value is the list of moves our
     * opponent played after the given move.
     * E.g. if the history of our opponent's moves was [R, R, P, S, R],
     * our map is:
     * <pre>
     * R -> [R, P]
     * P -> [S]
     * S -> [R]
     * </pre>
     * Then we look at the opponent's last turn and select a random value from the corresponding list (e.g. make a weighted guess).
     * This is the predicted next move of our opponent and we make the move that beats it.
     * If we don't have a history for some move - just make random move.
     */
    private static class MarkovChainStrategy implements Strategy {

        private final Function<Integer, Integer> gen; // random number generator

        private MarkovChainStrategy(Function<Integer, Integer> gen) {
            this.gen = gen;
        }

        @Override
        public Move makeMove(List<Move> moves) {
            EnumMap<Move, List<Move>> markovChain = buildMarkovChain(moves);
            Move last = getLast(moves); // this should be safe
            List<Move> possibleMoves = markovChain.get(last);
            Move predictedMove = getRandomElement(possibleMoves, gen).orElse(randomMove(gen));
            return predictedMove.losesTo();
        }


        private EnumMap<Move, List<Move>> buildMarkovChain(List<Move> moves) {
            EnumMap<Move, List<Move>> markovChain = emptyMarkovChain();
            Streams.sliding(moves, 2)
                    .forEach(pair -> {
                        Move fst = pair.get(0);
                        Move snd = pair.get(1);
                        markovChain.get(fst).add(snd);
                    });
            return markovChain;
        }

        private EnumMap<Move, List<Move>> emptyMarkovChain() {
            EnumMap<Move, List<Move>> result = new EnumMap<>(Move.class);
            for (Move key : Move.values()) {
                result.put(key, new ArrayList<>());
            }
            return result;
        }
    }

    private static <T> Optional<T> getRandomElement(List<T> xs, Function<Integer, Integer> gen) {
        if (xs.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(xs.get(gen.apply(xs.size())));
    }

    private static Move randomMove(Function<Integer, Integer> gen) {
        return Move.values()[gen.apply(Move.values().length)];
    }

    private static <T> T getLast(List<T> xs) {
        return xs.get(xs.size() - 1);
    }
}
