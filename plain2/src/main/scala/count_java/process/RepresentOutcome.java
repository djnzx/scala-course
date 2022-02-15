package count_java.process;

import count_java.val.Node;
import count_java.val.RowInfo;

import java.util.stream.Stream;

import static count_java.util.Functions.lastChunk;

public class RepresentOutcome {
  /**
   * represent - lazy
   */
  public static Stream<RowInfo> represent(Node node) {
    return Stream.concat(
        Stream.of(new RowInfo(node.level, lastChunk(node.file), node.count)),
        node.children.stream()
            .flatMap(RepresentOutcome::represent)
    );
  }
}
