package max.rindon.ai;

import max.rindon.domain.Move;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static max.rindon.ai.Strategies.*;
import static max.rindon.domain.Move.*;
import static org.junit.jupiter.api.Assertions.*;

class StrategiesTest {

    private static final List<Move> MOVES = Arrays.asList(ROCK, PAPER, SCISSORS, PAPER, PAPER);

    @Test
    @DisplayName("Check constant strategies")
    void constantStrategies() {
        assertEquals(ROCK, ALWAYS_ROCK.makeMove(MOVES));
        assertEquals(PAPER, ALWAYS_PAPER.makeMove(MOVES));
        assertEquals(SCISSORS, ALWAYS_SCISSORS.makeMove(MOVES));
    }

    @Test
    @DisplayName("Random moves")
    void random() {
        Strategy random = Strategies.random();
        List<Move> randomMoves = IntStream.range(0, 100)
                .mapToObj(i -> random.makeMove(MOVES))
                .collect(toList());
        // something like assert4j would help here
        // this may actually fail if we get unlucky ;)
        assertTrue(randomMoves.stream().anyMatch(it -> it == ROCK));
        assertTrue(randomMoves.stream().anyMatch(it -> it == PAPER));
        assertTrue(randomMoves.stream().anyMatch(it -> it == SCISSORS));
    }

    @Test
    @DisplayName("Echo with no history makes default move")
    void echoDefault() {
        Move move = Strategies.echo(ROCK).makeMove(emptyList());
        assertEquals(ROCK, move);
    }

    @Test
    @DisplayName("Echo with history takes last move")
    void echo() {
        Move move = Strategies.echo(ROCK).makeMove(MOVES);
        assertEquals(PAPER, move);
    }

    @Test
    @DisplayName("LastLost with no history makes default move")
    void lastLostDefault() {
        Move move = Strategies.lastLost(PAPER).makeMove(emptyList());
        assertEquals(PAPER, move);
    }

    @Test
    @DisplayName("LastLost with history makes move that would have lost the last play")
    void lastLost() {
        Move move = Strategies.lastLost(PAPER).makeMove(MOVES);
        assertEquals(ROCK, move);
    }

    @Test
    @DisplayName("Makes a random choice of which Strategy to use each turn.")
    void toss() {
        Strategy toss = Strategies.toss(ALWAYS_ROCK, ALWAYS_PAPER);
        List<Move> tossMoves = IntStream.range(0, 100)
                .mapToObj(i -> toss.makeMove(MOVES))
                .collect(toList());
        // something like assert4j would help here
        assertTrue(tossMoves.stream().allMatch(it -> it == ROCK || it == PAPER));
    }

    @Test
    @DisplayName("Alternates between two given strategies")
    void alternate() {
        Strategy alternate = Strategies.alternate(ALWAYS_ROCK, ALWAYS_PAPER);
        Move firstMove = alternate.makeMove(emptyList());
        assertEquals(ROCK, firstMove);
        Move secondMove = alternate.makeMove(singletonList(ROCK));
        assertEquals(PAPER, secondMove);
        Move thirdMove = alternate.makeMove(Arrays.asList(ROCK, PAPER));
        assertEquals(ROCK, thirdMove);
    }

    @Test
    @DisplayName("Uses first strategy if this is first turn. Otherwise uses other strategy.")
    void firstThenOther() {
        Strategy firstThenOther = Strategies.firstThenOther(ALWAYS_ROCK, ALWAYS_SCISSORS);
        Move firstMove = firstThenOther.makeMove(emptyList());
        assertEquals(ROCK, firstMove);
        Move secondMove = firstThenOther.makeMove(singletonList(ROCK));
        assertEquals(SCISSORS, secondMove);
        Move thirdMove = firstThenOther.makeMove(Arrays.asList(ROCK, PAPER));
        assertEquals(SCISSORS, thirdMove);
    }

    @Test
    @DisplayName("Beats most frequent opponent move.")
    void beatMostFrequent() {
        Move move = Strategies.beatMostFrequent(ROCK).makeMove(MOVES);
        assertEquals(SCISSORS, move);
    }

    @Test
    @DisplayName("Uses Markov chain to predict next opponent move.")
    void markovChain() {
        Strategy markov = Strategies.markovChain(PAPER);

        List<Move> rocks = Arrays.asList(ROCK, ROCK, ROCK, ROCK, ROCK, ROCK);
        assertEquals(PAPER, markov.makeMove(rocks));


    }
}