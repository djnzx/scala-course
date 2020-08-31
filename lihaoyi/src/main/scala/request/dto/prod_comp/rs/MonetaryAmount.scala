package request.dto.prod_comp.rs

import java.util.Currency

case class MonetaryAmount(
  currency: Currency,
  value: BigDecimal
)
