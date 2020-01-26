package essential

object X220Task extends App {
  final case class Rational(numerator: Int, denominator: Int)

  object Rational {
    implicit val ordRational: Ordering[Rational] = Ordering.fromLessThan[Rational]((x: Rational, y: Rational) =>
      x.numerator * y.denominator < y.numerator * x.denominator
    )
  }

  assert(
    List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted ==
    List(Rational(1, 3), Rational(1, 2), Rational(3, 4))
  )
}
