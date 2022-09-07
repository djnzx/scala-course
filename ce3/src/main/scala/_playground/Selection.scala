package _playground

import cats.Order

case class HP(private val value: Int) extends AnyVal
object HP {
  implicit val ord: Order[HP] = Order.by(_.value)
}
case class USD(private val value: Int) extends AnyVal
object USD {
  implicit val ord: Order[USD] = Order.by(_.value)
}

/** https://antonkw.github.io/scala/selection-functions/ */
object Selection extends App {

  type J[R, A] = (A => R) => A
  type K[R, A] = (A => R) => R

  def maxWith[Entity, Property](
      entities: List[Entity],
      order: cats.Order[Property]
  ): J[Property, Entity] =
    (evaluate: Entity => Property) => entities.maxBy(evaluate)(order.toOrdering)

  case class BMW(model: String, power: HP, price: USD)
  object BMW {
    def of(model: String, power: Int, price: Int): BMW = new BMW(model, HP(power), USD(price))
  }

  val bmws = List(
    BMW.of("230i", 255, 38395),
    BMW.of("330e", 288, 46295),
    BMW.of("M550i", 523, 60945)
  )

  def bmwSelection[Property](implicit order: Order[Property]): J[Property, BMW] = maxWith[BMW, Property](bmws, order)

  val bmwSelectionByHp: J[HP, BMW] = bmwSelection[HP]

  // as a parameter - required function BMW => HP
  val powerfulBmw: BMW = bmwSelectionByHp(_.power)

  def overline[R, A]: J[R, A] => K[R, A] =
    (select: J[R, A]) =>
      (eval: A => R) => {
        val selected: A = select(eval)
        val evaluated: R = eval.apply(selected)
        evaluated
      }

  val maxPowerOfBmw: HP = overline(bmwSelectionByHp)(_.power)

  println(powerfulBmw)
  println(maxPowerOfBmw)
}
