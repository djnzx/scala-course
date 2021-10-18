package logistics.extract;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Match {

  /**
   * check matching from by using **one** pattern
   * @return Optional.of() - if value matched
   *         Optional.empty() - if no
   */
  public static Optional<String> one(final String origin, final Pattern pt) {
    final Matcher matcher = pt.matcher(origin);
    return matcher.find() ? Optional.of(matcher.group(0)) : Optional.empty();
  }

  /**
   * check matching from by using **many** patterns
   * @return Optional.of() - if value matched
   *         Optional.empty() - if no
   */
  public static Optional<String> many(final String origin, final Pattern[] pts) {
    for (Pattern pt: pts) {
      final Matcher matcher = pt.matcher(origin);
      if (matcher.find()) {
        return Optional.of(matcher.group(0));
      }
    }
    return Optional.empty();
  }

  public static Optional<String> extractEbayItem(final String origin) {
    return Match.one(origin, Pt.EBAY_ITEM_NUMBER);
  }

  public static Optional<String> normalize(final String origin) {
    final String replaced = Pt.TRACK_ALLOWED.matcher(origin).replaceAll("");
    return replaced.isEmpty() ? Optional.empty() : Optional.of(replaced);
  }


}
