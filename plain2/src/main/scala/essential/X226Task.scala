package essential

class X226Task extends App {

  final case class Order(units: Int, unitPrice: Double) {
    val totalPrice: Double = units * unitPrice
  }

  object Order {
    val o: Ordering[Order] = Ordering.fromLessThan[Order]{ (a, b) => a.totalPrice < b.totalPrice }
  }

  object OrderingOrderByUnitQty {
    val o: Ordering[Order] = Ordering.fromLessThan[Order] { (a, b) => a.units < b.units }
  }

  object OrderingOrderByUnitPrice {
    val o: Ordering[Order] = Ordering.fromLessThan[Order] { (a, b) => a.unitPrice < b.unitPrice }
  }
}
