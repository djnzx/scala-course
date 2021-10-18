package logistics.amt.number;

import java.util.Objects;

public class NumberDetailsEmpty extends NumberDetails {

  public final String oem;

  public NumberDetailsEmpty(String oem) {
    this.oem = oem;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NumberDetailsEmpty that = (NumberDetailsEmpty) o;

    return Objects.equals(oem, that.oem);
  }

  @Override
  public int hashCode() {
    return oem != null ? oem.hashCode() : 0;
  }

  @Override
  public String toString() {
    return String.format("NumberDetailsEmpty:[oem='%s']", oem);
  }

}
