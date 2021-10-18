package logistics.extract;

import java.util.regex.Pattern;

public class Pt {
  /**
   * allowed only: 0..9, A..Z, a..z, -, _
   */
  public static final Pattern TRACK_ALLOWED = Pattern.compile("[^\\dA-Za-z\\-_]");
  /**
   * everything between single quotes
   */
  public static final Pattern BTW_SINGLE_QUOTES = Pattern.compile("'(.*?)'");
  /**
   * Ebay Item Number
   * any 12 digit number
   * bounded by word delimiters
   */
  public static final Pattern EBAY_ITEM_NUMBER = Pattern.compile("\\b\\d{12}\\b");
  /**
   * Any Tracking Number
   */
  public static final Pattern ANY_TRACK = Pattern.compile("^.+$");
  /**
   * AutoMotoTrade tracking number
   */
  public static final Pattern[] TR_AMT = {
      Pattern.compile("^a\\d{2,9}$")
  };
  /**
   * UPS tracking number
   */
  public static final Pattern[] TR_UPS = {
      Pattern.compile("^1Z\\w{16}$")
  };
  /**
   * DHL tracking number
   */
  public static final Pattern[] TR_DHL = {
      Pattern.compile("^\\d{10}$")
  };
  /**
   * Amazon tracking number
   */
  public static final Pattern[] TR_AMAZON = {
      Pattern.compile("^TBA\\d{12}$")
  };
  /**
   * China tracking number
   */
  public static final Pattern[] TR_CHINA = {
      Pattern.compile("^[^\\d]{2}\\d{12}[^\\d]{2}$")
  };
  /**
   * USPS tracking number
   */
  public static final Pattern[] TR_USPS = {
      Pattern.compile("^\\d{20}$"),
      Pattern.compile("^3\\d{18}$"),
      Pattern.compile("^9[12345]\\d{20}$"),
      Pattern.compile("^420\\d{5}9[12345]\\d{20}$"),
      Pattern.compile("^9[23]\\d{24}$"),
      Pattern.compile("^\\d{2}\\d{9}[^\\d]{2}$")
  };
  /**
   * FedEx tracking number
   */
  public static final Pattern[] TR_FEDEX = {
      Pattern.compile("^\\d{12,13}$"),
      Pattern.compile("^\\d{15,16}$"),
      Pattern.compile("^6129\\d{16}$"),
      Pattern.compile("^9612\\d{18}$"),
      Pattern.compile("^\\d{34}$"),
  };

}
