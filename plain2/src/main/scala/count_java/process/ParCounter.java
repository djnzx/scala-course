package count_java.process;

import count_java.val.FNode;
import count_java.val.Node;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static count_java.util.Functions.lastChunk;

public class ParCounter {
  private final Function<File, Long> countFn;

  public ParCounter(Function<File, Long> countFn) {
    this.countFn = countFn;
  }

  /**
   * count - parallel
   */
  public Map<File, List<Long>> parCount(FNode tree) {
    return FolderCrawler.flatten(tree)
        .map(n -> n.file)
        .parallel()
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.mapping(countFn, Collectors.toList())
        ));
  }

  /**
   * combine tree and data calculated
   */
  public static Node enrich(FNode node, int level, Map<File, List<Long>> data) {
    if (node instanceof FNode.FFile) return new Node.NFile(node.file, level, f -> data.get(f).get(0));
    List<Node> children = node.children.stream()
        .map(sn -> enrich(sn, level + 1, data))
        .sorted(Comparator.comparing(sn -> lastChunk(sn.file)))
        .collect(Collectors.toList());
    return new Node.NFolder(node.file, level, children);
  }

}
