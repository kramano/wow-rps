package max.rindon.domain;

import max.rindon.ai.Strategy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class RockPaperScissors {

    private final Strategy aiStrategy;
    private List<Round> history;
    private EnumMap<Outcome, Integer> statistics;

    public RockPaperScissors(Strategy aiStrategy) {
        this.aiStrategy = aiStrategy;
        this.history = new ArrayList<>();
        this.statistics = initStatistics();
    }

    public Round playRound(Move playerMove) {
        List<Move> pastPlayerMoves = history.stream()
                .map(r -> r.playerMove)
                .collect(Collectors.toList());

        Move aiMove = aiStrategy.makeMove(pastPlayerMoves);
        Outcome outcome = Rules.evaluate(playerMove, aiMove);
        Round round = new Round(playerMove, aiMove, outcome);
        history.add(round);
        statistics.computeIfPresent(outcome, (k, v) -> v + 1);
        return round;
    }

    private EnumMap<Outcome, Integer> initStatistics() {
        EnumMap<Outcome, Integer> result = new EnumMap<>(Outcome.class);
        for (Outcome key : Outcome.values()) {
            result.put(key, 0);
        }
        return result;
    }


    public EnumMap<Outcome, Integer> getStatistics() {
        return statistics;
    }
}
