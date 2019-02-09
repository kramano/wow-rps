package max.rindon.ai;

import max.rindon.domain.Move;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface Strategy extends Function<List<Move>, Move> {

    @Override
    public Move apply(List<Move> moves);
}
