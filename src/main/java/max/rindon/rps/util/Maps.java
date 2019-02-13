package max.rindon.rps.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Utilities to simplify map initialization.
 */
public class Maps {

    private Maps() {}

    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new SimpleEntry<>(key, value);
    }

    public static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> entriesToMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }
}
