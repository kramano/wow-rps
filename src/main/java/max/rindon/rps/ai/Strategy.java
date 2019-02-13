package max.rindon.rps.ai;

import max.rindon.rps.domain.Move;

import java.util.List;

@FunctionalInterface
public interface Strategy {

    Move makeMove(List<Move> moves);
}
