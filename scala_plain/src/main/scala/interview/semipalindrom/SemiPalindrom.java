package interview.semipalindrom;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SemiPalindrom {
    public static void main(String[] args) {
        IntStream.rangeClosed(1,5)
                .mapToObj(current -> IntStream.rangeClosed(1,current).map(each -> each*current))
                .flatMap((Function<IntStream, Stream<Integer>>) IntStream::boxed)
                .forEach(i-> System.out.println(i));
    }
}
