package logistics.amt.number;

import java.util.Map;
import java.util.Objects;

/**
 * http://automototrade.com/?key=wsdl&getPriceByOem
 */
public class NumberDetailsFull extends NumberDetails {
  public final String oem;
  public final String oem_original;
  public final String replace;

  public final String description;
  public final String description_ru;

  public final String brand;
  public final String supplier;

  public final float weight;
  public final float core_price;
  public final float price_cntr;
  public final float price_avia;

  public NumberDetailsFull(Map<String, Object> origin) {
//    System.out.println(origin);
    // that the order how they represented in response
    this.description = (String) origin.get("descr");
    this.description_ru = (String) origin.get("descr_ru");
    this.core_price = (float) origin.get("core_price");
    this.price_cntr = (float) origin.get("list_price_cntr");
    this.supplier = (String) origin.get("supplier");
    this.oem_original = (String) origin.get("oem_original");
    this.oem = (String) origin.get("oem");
    this.replace = (String) origin.get("replace");
    this.weight = origin.get("weight") instanceof Integer ? Float.valueOf((Integer)origin.get("weight")) :
        origin.get("weight") instanceof Float ? (float) origin.get("weight") : 0;
    this.price_avia = (float) origin.get("list_price");
    this.brand = (String) origin.get("brand");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    NumberDetailsFull that = (NumberDetailsFull) o;

    if (Float.compare(that.weight, weight) != 0) return false;
    if (Float.compare(that.core_price, core_price) != 0) return false;
    if (Float.compare(that.price_cntr, price_cntr) != 0) return false;
    if (Float.compare(that.price_avia, price_avia) != 0) return false;
    if (!Objects.equals(oem_original, that.oem_original)) return false;
    if (!Objects.equals(replace, that.replace)) return false;
    if (!Objects.equals(description, that.description)) return false;
    if (!Objects.equals(description_ru, that.description_ru)) return false;
    if (!Objects.equals(brand, that.brand)) return false;
    return Objects.equals(supplier, that.supplier);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (oem_original != null ? oem_original.hashCode() : 0);
    result = 31 * result + (replace != null ? replace.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (description_ru != null ? description_ru.hashCode() : 0);
    result = 31 * result + (brand != null ? brand.hashCode() : 0);
    result = 31 * result + (supplier != null ? supplier.hashCode() : 0);
    result = 31 * result + (weight != +0.0f ? Float.floatToIntBits(weight) : 0);
    result = 31 * result + (core_price != +0.0f ? Float.floatToIntBits(core_price) : 0);
    result = 31 * result + (price_cntr != +0.0f ? Float.floatToIntBits(price_cntr) : 0);
    result = 31 * result + (price_avia != +0.0f ? Float.floatToIntBits(price_avia) : 0);
    return result;
  }

  @Override
  public String toString() {
    return "NumberDetailsFull: [" +
        "oem='" + oem + '\'' +
        ", brand='" + brand + '\'' +
        ", replace='" + replace + '\'' +
        ", oem_original='" + oem_original + '\'' +
        ", description='" + description + '\'' +
        ", description_ru='" + description_ru + '\'' +
        ", supplier='" + supplier + '\'' +
        ", weight=" + weight +
        ", core_price=" + core_price +
        ", price_cntr=" + price_cntr +
        ", price_avia=" + price_avia +
        ']';
  }
}
