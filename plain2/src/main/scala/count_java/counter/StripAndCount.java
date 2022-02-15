package count_java.counter;

import count_java.count.Count;
import count_java.strip.Strip;
import count_java.strip.state.LineState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static count_java.util.Fold.fold;

public class StripAndCount {
  private final Strip stripper;
  private final Count counter;

  public StripAndCount(Strip stripper, Count counter) {
    this.stripper = stripper;
    this.counter = counter;
  }

  public FileState fold_file(FileState acc, String line) {
    LineState ls = LineState.fresh(line, acc.inBlock);
    while (!ls.isDone()) ls = stripper.apply(ls);
    return acc.updated(counter.count(ls.result()), ls.inBlock);
  }

  public long count(Stream<String> stream) {
    FileState zero = FileState.fresh();
    return fold(stream, zero, this::fold_file).count;
  }

  public long count(File file) {
    try (Stream<String> stream = Files.lines(file.toPath())) {
      return count(stream);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException();
    }
  }
}
