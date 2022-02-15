package count_java;

import count_java.process.FolderCrawler;
import count_java.process.ParCounter;
import count_java.val.FNode;
import count_java.val.Node;
import count_java.val.RowInfo;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static count_java.process.RepresentOutcome.represent;

public class LineCounter {
  private final FolderCrawler treeBuilder;
  private final ParCounter counter;

  public LineCounter(Function<File, Boolean> fileFilter, Function<File, Long> countFn) {
    this.treeBuilder = new FolderCrawler(fileFilter);
    this.counter = new ParCounter(countFn);
  }

  public Stream<String> processParallel(File root) {
    FNode tree = treeBuilder.scan(root);
    Map<File, List<Long>> data = counter.parCount(tree);
    Node enriched = ParCounter.enrich(tree, 0, data);
    return represent(enriched)
        .map(RowInfo::toString);
  }

}
