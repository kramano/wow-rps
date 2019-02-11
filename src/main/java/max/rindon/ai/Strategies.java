package max.rindon.ai;

import max.rindon.domain.Move;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static max.rindon.domain.Move.ROCK;

/**
 * Static factories and some combinators for strategies.
 * Combinators allow to create more complex strategies from simpler ones.
 * For example, {@link Strategies#random} strategy
 * can be implemented in terms of constant strategies using {@link Strategies#toss(Strategy, Strategy) } combinator.
 */
public class Strategies {

    private static final Random RANDOM = new Random();

    // some dumb strategies for testing
    public static final Strategy ALWAYS_ROCK = always(Move.ROCK);
    public static final Strategy ALWAYS_PAPER = always(Move.PAPER);
    public static final Strategy ALWAYS_SCISSORS = always(Move.SCISSORS);

    /**
     * Always make given move.
     *
     * @param move Move to make each time.
     * @return Strategy that makes random moves.
     */
    public static Strategy always(Move move) {
        return (noMatterWhat -> move);
    }

    /**
     * Make a random move. We cannot do better in a perfect world.
     *
     * @return Strategy that makes random moves.
     */
    public static Strategy random() {
        return (ignored -> Move.values()[RANDOM.nextInt(Move.values().length)]);
    }

    /**
     * Echo the previous move; We have to supply default Move.
     *
     * @param defaultMove move to use if there are no previous moves
     * @return Strategy that repeats last opponent move
     */
    public static Strategy echo(Move defaultMove) {
        return (moves -> {
            if (moves.isEmpty()) {
                return defaultMove;
            } else return moves.get(moves.size() - 1);
        });
    }

    /**
     * Return a move that would have lost the last play;
     *
     * @param defaultMove move to use if there are no previous moves
     * @return Strategy that returns a move that would have lost the last play
     */
    public static Strategy lastLost(Move defaultMove) {
        return (moves -> {
            if (moves.isEmpty()) {
                return defaultMove;
            } else return moves.get(moves.size() - 1).beats();
        });
    }

    /**
     * Beats most frequent opponent move.
     *
     * @param defaultMove move to use if there are no previous moves
     * @return Strategy that returns a move that beats most frequent opponent move.
     */
    public static Strategy beatMostFrequent(Move defaultMove) {
        return (moves -> moves.stream()
                .collect(Collectors.groupingBy(identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .map(Move::losesTo)
                .orElse(defaultMove));
    }

    /**
     * Make a random choice of which Strategy to use each turn.
     */
    public static Strategy toss(Strategy first, Strategy second) {
        int rand = RANDOM.nextInt(2);
        return (moves -> {
            if (rand == 0) {
                return first.makeMove(moves);
            } else {
                return second.makeMove(moves);
            }
        });
    }

    /**
     * Alternate strategies each turn.
     */
    public static Strategy alternate(Strategy first, Strategy second) {
        return (moves -> {
            if (moves.size() % 2 == 0) {
                return first.makeMove(moves);
            } else {
                return second.makeMove(moves);
            }
        });
    }

    /**
     * Use first strategy if this is first turn. Otherwise use the other strategy.
     * This may be useful when other strategy requires history.
     * @param first Strategy to use on first turn
     * @param other Strategy to use for remaining turns
     */
    public static Strategy firstThenOther(Strategy first, Strategy other) {
        return (moves -> {
            if (moves.size() == 0) {
                return first.makeMove(moves);
            } else {
                return other.makeMove(moves);
            }
        });
    }


}
