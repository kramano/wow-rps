package max.rindon.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lists {

    private Lists() {}

    private static final Random RANDOM = new Random();

    public static <T> List<T> append(List<T> xs, T x) {
        ArrayList<T> result = new ArrayList<>(xs);
        result.add(x);
        return result;
    }

    public static <T> T getLast(List<T> xs) {
        return xs.get(xs.size() - 1);
    }

    public static <T> T getRandomElement(List<T> xs) {
        return xs.get(RANDOM.nextInt(xs.size()));
    }
}
