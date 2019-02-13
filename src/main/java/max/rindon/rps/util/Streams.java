package max.rindon.rps.util;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Streams {

    private Streams() {}

    // replace with spliterator implementation?
    public static <T> Stream<List<T>> sliding(List<T> list, int size) {
        if (size > list.size()) {
            return Stream.empty();
        } else {
            return IntStream.range(0, list.size() - size + 1)
                    .mapToObj(start -> list.subList(start, start + size));
        }
    }
}
