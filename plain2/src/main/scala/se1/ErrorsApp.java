package se1;

import io.vavr.control.Either;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

interface OurError {}
class Error1 implements OurError {}
class Error2 implements OurError {}
class Error3 implements OurError {}

public class ErrorsApp {

  /**
   * Integer
   * null
   * exception
   */
  Integer div(int a, int b) {
    return a / b;
  }

  int min(List<Integer> data) {
    throw new IllegalArgumentException("not iplemented");
  }

  Optional<Integer> safeMin(List<Integer> data) {
    throw new IllegalArgumentException("not iplemented");
  }

  static Either<String, Integer> smartMin(List<Integer> data) {
    Optional<Integer> minOpt = data.stream().min(Integer::compareTo);
    // happy
    if (minOpt.isPresent()) {
      Integer min = minOpt.get();
      Either<String, Integer> e1 = Either.right(min);
      return e1;
    }
    // error
    return Either.left("Dataset is empty");
  }

  static Either<OurError, String> computationOne() {
    return Either.right("Hello");
  }

  static Integer computationTwo(String s) {
    return s.length();
  }

  static Either<OurError, Double> computationThree(int i) {
    return Either.left(new Error3());
  }

  public static void main2(String[] args) {
    Either<OurError, Double> either = computationOne()
      .map(ErrorsApp::computationTwo)
      .flatMap(ErrorsApp::computationThree);
  }

  public static void main1(String[] args) {
    Either<String, Integer> r1 = smartMin(Arrays.asList()); // Left("Dataset is empty")
    Either<String, Integer> r1mapped = r1.map(x -> x + 1);  // Left("Dataset is empty")

    Either<String, Integer> r2 = smartMin(Arrays.asList(12, 34, 56, 10)); // Right(10)
    Either<String, Integer> r2mapped = r2.map(x -> x + 1);
    Either<String, Integer> map = r2.flatMap(x -> {
      try {
        return Either.right(x / 0);
      } catch (Exception ex) {
        return Either.left("Division by 0");
      }
    });
  }

  public static void main4(String[] args) throws IOException {
    File file = new File("111");
    FileInputStream fis = new FileInputStream(file);
    int read = fis.read();
    fis.close();
  }

  class Resource<R> {
    private final Supplier<R> acquire; // () => R
    private final Consumer<R> release; // (R) => void

    Resource(Supplier<R> acquire, Consumer<R> release) {
      this.acquire = acquire;
      this.release = release;
    }

    <A> A use(Function<R, A> f) {
      R resource = acquire.get();
      A applied = f.apply(resource);
      release.accept(resource);
      return applied;
    }

  }

  public static void main(String[] args) {
//    Resource<FileInputStream> fis = new Resource<FileInputStream>(
//      () -> {
//      File file = new File("111");
//      return new FileInputStream(file);
//    },
//      fis -> fis.close()
//    );
//
//    Integer readed = fis.use(r -> r.read());


  }


}
