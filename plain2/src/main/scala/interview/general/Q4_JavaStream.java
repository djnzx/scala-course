package interview.general;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Q4_JavaStream {

    public static void main(String[] args) {
        record Pair(char c, int idx) { }
        String in = "Hello World";
        Map<Character, List<Integer>> collected = Stream.of(in)
          .flatMap(s ->
             IntStream
               .range(0, in.length())
               .mapToObj(i -> new Pair(Character.toLowerCase(in.charAt(i)), i + 1))
          )
          .collect(
            Collectors.groupingBy(p -> p.c, Collectors.mapping(p -> p.idx, Collectors.toList()))
          );
        System.out.println(collected);
    }

}
