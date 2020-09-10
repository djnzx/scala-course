package hackerrank;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class CollectorsEx {
  public static void main(String[] args) {
    String collected = Stream.generate(() -> (int) (Math.random() * 26 + 'A'))
      .limit(50)
      .collect(new Collector<Integer, StringBuilder, String>() {
        @Override
        public Supplier<StringBuilder> supplier() {
          return StringBuilder::new;
        }

        @Override
        public BiConsumer<StringBuilder, Integer> accumulator() {
          return (sb, i) -> sb.append((char)i.intValue());
        }

        @Override
        public BinaryOperator<StringBuilder> combiner() {
          return StringBuilder::append;
        }

        @Override
        public Function<StringBuilder, String> finisher() {
          return StringBuilder::toString;
        }

        @Override
        public Set<Characteristics> characteristics() {
          return Collections.emptySet();
        }
      });
    System.out.println(collected);
  }
}
