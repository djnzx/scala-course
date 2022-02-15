package count_java.strip.other;

import count_java.strip.Patterns;
import count_java.strip.Strip;
import count_java.strip.state.LineState;

/**
 * StripLineOnlyComments implementation
 * removes line comments ONLY
 */
public final class Strip2LineOnlyComments implements Strip, Patterns {
  @Override
  public LineState apply(LineState ls) {
    final int ln_pos = ls.find(LINE);
    return ln_pos >= 0 ?
        ls.saveTo(ln_pos).skipRest() :
        ls.saveRest();
  }
}
