package count_java.strip.other;

import count_java.strip.Strip;
import count_java.strip.state.LineState;

/**
 * StripNothing implementation
 * just keeps original text
 */
public final class Strip1Nothing implements Strip {
  @Override
  public LineState apply(LineState ls) {
    return ls.saveRest();
  }
}
