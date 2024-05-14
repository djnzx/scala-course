package app.model

import scala.math.Ordering.Implicits.infixOrderingOps

sealed abstract class Predicate[-A](val test: A => Boolean)

object Predicate {

  case class IsEqualTo[A](value: A)
      extends Predicate[A](_ == value)

  case class IsNotEqualTo[A](value: A)
      extends Predicate[A](_ != value)

  case class IsGreaterThenOrEqualTo[A: Ordering](value: A)
      extends Predicate[A](_ >= value)

  case class IsGreaterThen[A: Ordering](value: A)
      extends Predicate[A](_ > value)

  case class IsLessThenOrEqualTo[A: Ordering](value: A)
      extends Predicate[A](_ <= value)

  case class IsLessThen[A: Ordering](value: A)
      extends Predicate[A](_ < value)

  case class InSet[A](values: Set[A])
      extends Predicate[A](values.contains)
  object InSet {
    def apply[A](value: A, values: A*): InSet[A] = InSet(Set((value +: values): _*))
  }

  case class NotInSet[A](values: Set[A])
      extends Predicate[A](x => !values.contains(x))
  object NotInSet {
    def apply[A](value: A, values: A*): NotInSet[A] = NotInSet(Set((value +: values): _*))
  }

  type PredConstr1 = PropVal => Predicate[PropVal]
  type PredConstrN = Set[PropVal] => Predicate[PropVal]

  def eq: PredConstr1 = IsEqualTo[PropVal]
  def neq: PredConstr1 = IsNotEqualTo[PropVal]
  def gte: PredConstr1 = IsGreaterThenOrEqualTo[PropVal]
  def gt: PredConstr1 = IsGreaterThen[PropVal]
  def lte: PredConstr1 = IsLessThenOrEqualTo[PropVal]
  def lt: PredConstr1 = IsLessThen[PropVal]
  def in: PredConstrN = InSet[PropVal]
  def notin: PredConstrN = NotInSet[PropVal]

}
