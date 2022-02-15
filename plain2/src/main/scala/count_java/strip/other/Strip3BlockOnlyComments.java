package count_java.strip.other;

import count_java.strip.Patterns;
import count_java.strip.Strip;
import count_java.strip.state.LineState;

/**
 * StripBlockOnlyComments implementation
 * removes block comments ONLY
 */
public final class Strip3BlockOnlyComments implements Strip, Patterns {
  @Override
  public LineState apply(LineState ls) {
    if (ls.inBlock) {
      final int cl_pos = ls.find(CLOSE);
      return cl_pos >= 0 ?
          ls.moveTo(cl_pos).shift(CLOSE.length()).swBlock() :
          ls.skipRest();
    }
    final int op_pos = ls.find(OPEN);
    return op_pos >= 0 ?
        ls.saveTo(op_pos).shift(OPEN.length()).swBlock() :
        ls.saveRest();
  }
}
