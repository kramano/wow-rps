package max.rindon.ai;

import max.rindon.domain.Move;
import max.rindon.util.Lists;
import max.rindon.util.Streams;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static max.rindon.util.Lists.getLast;

/**
 * Static factories and some combinators for strategies.
 * Combinators allow to create more complex strategies from simpler ones.
 * For example, {@link Strategies#random} strategy
 * could be implemented in terms of constant strategies using {@link Strategies#toss(Strategy, Strategy) } combinator.
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
     * Make a random move. It is impossible to gain an advantage over a truly random opponent.
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
            } else return getLast(moves).beats();
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

    public static Strategy markovChain(Move defaultMove) {
        return (moves -> {
            if (moves.isEmpty()) {
                return defaultMove;
            } else return new MarkovChainStrategy().makeMove(moves);
        });
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

    public static class MarkovChainStrategy implements Strategy {

        @Override
        public Move makeMove(List<Move> moves) {
            EnumMap<Move, List<Move>> markovChain = buildMarkovChain(moves);
            Move last = getLast(moves);
            List<Move> possibleMoves = markovChain.get(last);// TODO this may be empty
            Move predictedMove = Lists.getRandomElement(possibleMoves);
            return predictedMove.losesTo();
        }


        private EnumMap<Move, List<Move>> buildMarkovChain(List<Move> moves) {
            EnumMap<Move, List<Move>> markovChain = emptyMarkovChain();
            Streams.sliding(moves, 2)
                    .forEach(pair -> {
                        Move fst = pair.get(0);
                        Move snd = pair.get(1);
                        markovChain.compute(fst, (k, v) -> Lists.append(v, snd));
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
}
