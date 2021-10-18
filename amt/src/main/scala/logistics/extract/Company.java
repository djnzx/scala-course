package logistics.extract;

public class Company {
  public final String number;
  public final long id;
  public final String name;

  private Company(String number, long id, String name) {
    this.number = number;
    this.id = id;
    this.name = name;
  }

  public static Company UPS(String number) {
    return new Company(number, 1, "UPS");
  }

  public static Company FedEx(String number) {
    return new Company(number, 2, "FedEx");
  }

  public static Company USPS(String number) {
    return new Company(number, 3, "USPS");
  }

  public static Company DHL(String number) {
    return new Company(number, 4, "DHL");
  }

  public static Company Amazon(String number) {
    return new Company(number, 5, "Amazon Logistics");
  }

  public static Company AMT(String number) {
    return new Company(number, 6, "Auto Moto Trade");
  }

  public static Company Noname(String number) {
    return new Company(number, 7, "Noname");
  }

  public static Company Unknown(String number) {
    return new Company(number, 8, "Unknown");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Company)) return false;

    Company company = (Company) o;

    if (id != company.id) return false;
    if (!number.equals(company.number)) return false;
    return name.equals(company.name);
  }

  @Override
  public int hashCode() {
    int result = number.hashCode();
    result = 31 * result + (int) (id ^ (id >>> 32));
    result = 31 * result + name.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("Company:[%d:%s:%s]", id, number, name);
  }
}
