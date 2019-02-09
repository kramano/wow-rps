package max.rindon.util;

import max.rindon.domain.Move;
import max.rindon.domain.Outcome;
import max.rindon.domain.Round;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Temp {

    public static void main(String[] args) {
        List<Round> rounds = Arrays.asList(
                new Round(Move.PAPER, Move.PAPER, Outcome.DRAW),
                new Round(Move.PAPER, Move.ROCK, Outcome.WIN),
                new Round(Move.ROCK, Move.SCISSORS, Outcome.WIN)
        );

        Map<Outcome, Long> stats = rounds.stream()
                .collect(Collectors
                        .groupingBy(r -> r.outcome, Collectors.counting()));

        System.out.println(stats);
    }
}
