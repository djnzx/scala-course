package hackerrank.d200515;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class SherlockAndTheValidStringAppJava {

    public String isValid(String s) {
        Map<Character, Long> letters = s.chars()
                .mapToObj(x -> (char) x)
                .collect(groupingBy(a -> a, counting()));

        Map<Long, Long> freq = letters.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(groupingBy(a -> a, counting()));

        if (freq.size() == 1) return "YES";
        if (freq.size() > 2) return "NO";

        // sorted == 2
        List<Map.Entry<Long, Long>> sorted = freq.entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
                .collect(Collectors.toList());

        Map.Entry<Long, Long> s0 = sorted.get(0);
        Map.Entry<Long, Long> s1 = sorted.get(1);

        if ((s0.getKey() - s1.getKey() == 1)
                && s0.getValue() == 1) return "YES";

        if (s0.getKey() == 1
                && s0.getValue() == 1) return "YES";

        return "NO";
    }

}
