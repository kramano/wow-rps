package max.rindon.rps.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static max.rindon.rps.domain.Move.*;
import static max.rindon.rps.domain.Outcome.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RulesTest {

    @ParameterizedTest
    @MethodSource("movesAndOutcomesProvider")
    @DisplayName("Check game rules")
    void testGameRules(Move first, Move second, Outcome expected) {
        Outcome outcome = Rules.evaluate(first, second);
        assertEquals(outcome, expected);
    }

    static Stream<Arguments> movesAndOutcomesProvider() {
        return Stream.of(
                arguments(ROCK, PAPER, LOSS),
                arguments(ROCK, ROCK, DRAW),
                arguments(ROCK, SCISSORS, WIN),
                arguments(PAPER, SCISSORS, LOSS),
                arguments(PAPER, PAPER, DRAW),
                arguments(PAPER, ROCK, WIN),
                arguments(SCISSORS, ROCK, LOSS),
                arguments(SCISSORS, SCISSORS, DRAW),
                arguments(SCISSORS, PAPER, WIN)
        );
    }
}