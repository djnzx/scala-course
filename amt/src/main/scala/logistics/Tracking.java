package logistics;

import logistics.extract.Match;
import logistics.extract.Pt;
import logistics.extract.Company;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Tracking {

  private final static List<Function<String, Optional<Company>>> parsers = Arrays.asList(
      s -> Match.many(s, Pt.TR_UPS).map(Company::UPS),
      s -> Match.many(s, Pt.TR_FEDEX).map(Company::FedEx),
      s -> Match.many(s, Pt.TR_USPS).map(Company::USPS),
      s -> Match.many(s, Pt.TR_DHL).map(Company::DHL),
      s -> Match.many(s, Pt.TR_AMAZON).map(Company::Amazon),
      s -> Match.many(s, Pt.TR_AMT).map(Company::AMT),
      s -> Match.many(s, Pt.TR_CHINA).map(Company::Noname)
  );

  /**
   * it parses given String to Company
   * if no company found - return Company.Unknown
   */
  public static Company parse(String origin) {
    return parsers.stream()
        .map(f -> f.apply(origin))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElseGet(() -> Company.Unknown(origin));
  }

}
