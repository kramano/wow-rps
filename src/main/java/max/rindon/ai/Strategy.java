package max.rindon.ai;

import max.rindon.domain.Move;

import java.util.List;

@FunctionalInterface
public interface Strategy {

    public Move makeMove(List<Move> moves);
}
