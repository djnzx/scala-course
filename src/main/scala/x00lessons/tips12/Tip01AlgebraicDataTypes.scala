package x00lessons.tips12

object Tip01AlgebraicDataTypes extends App {
  // Model with Algebraic data types

  // Product
  case class Person(name: String, age: Int)
  // Sum
  sealed trait Contract
  case class Email(value: String) extends Contract
  case class Phone(value: Phone) extends Contract

  final case class Event(id: Int, name: String, payload: Payload)

  sealed trait Payload
  object Payload {
    final case class Purchase(details: String) extends Payload
    final case class Refund(order_id: Int) extends Payload
  }
  def changeName(event: Event, newName: String): Event =
    event.copy(name = newName)

  val p1: Payload = Payload.Purchase("Chrysler order")
  val p2: Payload = Payload.Refund(42)
  val e1 = Event(1, "Event1", p1)
  val e2 = Event(2, "Event2", p2)
  println(e1)
  println(e2)
  val e11 = changeName(e1, "Event11")
  e1.payload match {
    case Payload.Purchase(details) => println(s"It is Purchase: $details")
    case Payload.Refund(order_id)  => println(s"It is Refund: $order_id")
  }


}
