package max.rindon.rps.domain;

import max.rindon.rps.ai.Strategy;

import java.util.*;
import java.util.stream.Collectors;

public class RockPaperScissors {

    private final Strategy aiStrategy;
    private final List<Round> history;
    private final EnumMap<Outcome, Integer> statistics;

    public RockPaperScissors(Strategy aiStrategy) {
        this.aiStrategy = aiStrategy;
        this.history = new ArrayList<>();
        this.statistics = initStatistics();
    }

    public Round playRound(Move playerMove) {
        // Strategies could (and probably should)use full history,
        // but all our strategies use only opponent moves.
        List<Move> pastPlayerMoves = getPlayerMoves();
        Move aiMove = aiStrategy.makeMove(pastPlayerMoves);
        Outcome outcome = Rules.evaluate(playerMove, aiMove);
        Round round = new Round(playerMove, aiMove, outcome);
        history.add(round);
        statistics.computeIfPresent(outcome, (k, v) -> v + 1);
        return round;
    }

    private List<Move> getPlayerMoves() {
        return Collections.unmodifiableList(history.stream()
                    .map(r -> r.playerMove)
                    .collect(Collectors.toList()));
    }

    private EnumMap<Outcome, Integer> initStatistics() {
        EnumMap<Outcome, Integer> result = new EnumMap<>(Outcome.class);
        for (Outcome key : Outcome.values()) {
            result.put(key, 0);
        }
        return result;
    }

    public Map<Outcome, Integer> getStatistics() {
        return Collections.unmodifiableMap(statistics);
    }
}
