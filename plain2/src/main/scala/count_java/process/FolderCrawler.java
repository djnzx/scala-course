package count_java.process;

import count_java.val.FNode;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static count_java.util.Functions.safeStream;

public class FolderCrawler {
  private final Function<File, Boolean> filter;

  public FolderCrawler(Function<File, Boolean> fileFilter) {
    this.filter = fileFilter;
  }

  private Optional<FNode> scanr(File file) {
    if (file.isFile() && filter.apply(file)) return Optional.of(new FNode.FFile(file));
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      List<FNode> nodes = safeStream(files)
          .map(this::scanr)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .filter(n ->
              n instanceof FNode.FFile ||
              n instanceof FNode.FFolder && !n.children.isEmpty())
          .collect(Collectors.toList());
      return Optional.of(new FNode.FFolder(file, nodes));
    }
    return Optional.empty();
  }

  public FNode scan(File file) {
    return scanr(file).orElseThrow(() -> new IllegalArgumentException("empty folder given"));
  }

  public static Stream<FNode> flatten(FNode node) {
    return Stream.concat(
        Stream.of(node),
        node.children.stream().flatMap(FolderCrawler::flatten)
    ).filter(n -> n instanceof FNode.FFile);
  }

}
