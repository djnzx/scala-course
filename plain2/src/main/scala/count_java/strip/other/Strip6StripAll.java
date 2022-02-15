package count_java.strip.other;

import count_java.strip.Strip;
import count_java.strip.state.LineState;

/**
 * StripAll implementation
 * just throws everything
 */
public final class Strip6StripAll implements Strip {
  @Override
  public LineState apply(LineState ls) {
    return ls.skipRest();
  }
}
