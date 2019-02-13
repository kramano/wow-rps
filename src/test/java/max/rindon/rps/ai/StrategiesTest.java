package max.rindon.rps.ai;

import max.rindon.rps.domain.Move;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StrategiesTest {

    private static final List<Move> MOVES = Arrays.asList(Move.ROCK, Move.PAPER, Move.SCISSORS, Move.PAPER, Move.PAPER);

    @Test
    @DisplayName("Check constant strategies")
    void constantStrategies() {
        assertEquals(Move.ROCK, Strategies.ALWAYS_ROCK.makeMove(MOVES));
        assertEquals(Move.PAPER, Strategies.ALWAYS_PAPER.makeMove(MOVES));
        assertEquals(Move.SCISSORS, Strategies.ALWAYS_SCISSORS.makeMove(MOVES));
    }

    @Test
    @DisplayName("Random moves")
    void random() {
        Strategy random = Strategies.random((x -> 0)); // always take first
        Move move = random.makeMove(emptyList());
        assertEquals(Move.ROCK, move);
    }

    @Test
    @DisplayName("Echo with no history makes default move")
    void echoDefault() {
        Move move = Strategies.echo(Strategies.ALWAYS_ROCK).makeMove(emptyList());
        assertEquals(Move.ROCK, move);
    }

    @Test
    @DisplayName("Echo with history takes last move")
    void echo() {
        Move move = Strategies.echo(Strategies.ALWAYS_ROCK).makeMove(MOVES);
        assertEquals(Move.PAPER, move);
    }

    @Test
    @DisplayName("LastLost with no history makes default move")
    void lastLostDefault() {
        Move move = Strategies.lastLost(Strategies.ALWAYS_PAPER).makeMove(emptyList());
        assertEquals(Move.PAPER, move);
    }

    @Test
    @DisplayName("LastLost with history makes move that would have lost the last play")
    void lastLost() {
        Move move = Strategies.lastLost(Strategies.ALWAYS_PAPER).makeMove(MOVES);
        assertEquals(Move.ROCK, move);
    }

    @Test
    @DisplayName("Makes a random choice of which Strategy to use each turn.")
    void toss() {
        Strategy selectFirst = Strategies.toss(Strategies.ALWAYS_ROCK, Strategies.ALWAYS_PAPER, (x -> 0));
        Move firstStrategyMove = selectFirst.makeMove(MOVES);
        assertEquals(Move.ROCK, firstStrategyMove);

        Strategy selectSecond = Strategies.toss(Strategies.ALWAYS_ROCK, Strategies.echo(Strategies.ALWAYS_ROCK), (x -> 1));
        Move secondStrategyMove = selectSecond.makeMove(MOVES);
        assertEquals(secondStrategyMove, Move.PAPER);
    }

    @Test
    @DisplayName("Alternates between two given strategies")
    void alternate() {
        Strategy alternate = Strategies.alternate(Strategies.ALWAYS_ROCK, Strategies.ALWAYS_PAPER);
        Move firstMove = alternate.makeMove(emptyList());
        assertEquals(Move.ROCK, firstMove);
        Move secondMove = alternate.makeMove(singletonList(Move.ROCK));
        assertEquals(Move.PAPER, secondMove);
        Move thirdMove = alternate.makeMove(Arrays.asList(Move.ROCK, Move.PAPER));
        assertEquals(Move.ROCK, thirdMove);
    }

    @Test
    @DisplayName("Uses first strategy if this is first turn. Otherwise uses other strategy.")
    void firstThenOther() {
        Strategy firstThenOther = Strategies.firstThenOther(Strategies.ALWAYS_ROCK, Strategies.ALWAYS_SCISSORS);
        Move firstMove = firstThenOther.makeMove(emptyList());
        assertEquals(Move.ROCK, firstMove);
        Move secondMove = firstThenOther.makeMove(singletonList(Move.ROCK));
        assertEquals(Move.SCISSORS, secondMove);
        Move thirdMove = firstThenOther.makeMove(Arrays.asList(Move.ROCK, Move.PAPER));
        assertEquals(Move.SCISSORS, thirdMove);
    }

    @Test
    @DisplayName("Beats most frequent opponent move.")
    void beatMostFrequent() {
        Move move = Strategies.beatMostFrequent(Strategies.ALWAYS_ROCK).makeMove(MOVES);
        assertEquals(Move.SCISSORS, move);
    }

    @Test
    @DisplayName("Uses Markov chain to predict next opponent move.")
    void markovChain() {
        Strategy markov = Strategies.markovChain(Strategies.ALWAYS_PAPER, (x -> 0));

        List<Move> rocks = Arrays.asList(Move.ROCK, Move.ROCK, Move.ROCK, Move.ROCK, Move.ROCK, Move.ROCK);
        assertEquals(Move.PAPER, markov.makeMove(rocks));

        /* for MOVES we should have:
        * R -> [P]
        * P -> [S, P]
        * S -> [P]
        */
        assertEquals(Move.ROCK, markov.makeMove(MOVES));
    }

}