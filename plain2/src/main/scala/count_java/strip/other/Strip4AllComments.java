package count_java.strip.other;

import count_java.strip.Patterns;
import count_java.strip.Strip;
import count_java.strip.state.LineState;

/**
 * StripAllComments implementation
 * removes line comments
 * removes block comments
 * based on StripBlockOnlyComments
 */
public final class Strip4AllComments implements Strip, Patterns {

  private boolean isOpen(int op_pos, int ln_pos) {
    return op_pos >= 0 && ln_pos >= 0 && op_pos < ln_pos
        || op_pos >= 0 && ln_pos <  0;
  }

  private boolean isLine(int op_pos, int ln_pos) {
    return op_pos >= 0 && ln_pos >= 0 && ln_pos < op_pos
        || op_pos <  0 && ln_pos >= 0;
  }

  public LineState apply(LineState ls) {
    if (ls.inBlock) {
      final int cl_pos = ls.find(CLOSE);
      return cl_pos >= 0 ? ls.moveTo(cl_pos).shift(CLOSE.length()).swBlock() :
                           ls.skipRest();
    }
    final int op_pos = ls.find(OPEN);
    final int ln_pos = ls.find(LINE);
    return
        isOpen(op_pos, ln_pos) ? ls.saveTo(op_pos).shift(OPEN.length()).swBlock() :
        isLine(op_pos, ln_pos) ? ls.saveTo(ln_pos).skipRest() :
                                 ls.saveRest();
  }
}
