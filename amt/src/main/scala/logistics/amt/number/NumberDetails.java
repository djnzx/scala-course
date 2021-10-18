package logistics.amt.number;

import java.util.Map;

public class NumberDetails {

  private final static String NOT_FOUND = "No Part Found";
  private final static String FIELD_DESCR = "descr";
  private final static String FIELD_OEM = "oem";

  @SuppressWarnings("unchecked")
  public static NumberDetails from(Object origin) {
    return from((Map<String, Object>)origin);
  }

  public static NumberDetails from(Map<String, Object> origin) {
    return NOT_FOUND.equals(origin.get(FIELD_DESCR))
        ? new NumberDetailsEmpty((String) origin.get(FIELD_OEM))
        : new NumberDetailsFull(origin);
  }

}
