package a_interview;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntStreamApp {

    public static void main(String[] args) {
        List<Integer> f1 = IntStream.range(1, 10).boxed()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println(f1);

        List<Integer> f2 = IntStream.range(1, 10).boxed()
                .flatMap(n -> n % 2 == 0 ? Stream.of(n) : Stream.empty())
                .collect(Collectors.toList());
        System.out.println(f2);

    }

}
