package pfps.ref

import eu.timepit.refined.api.{Refined, Validate}

object NonNegativeR {

  sealed trait NonNegative

  implicit val validateNonNegative: Validate.Plain[Double, NonNegative] =
    Validate.fromPredicate(
      d => d >= 0,
      d => s"$d is not non-negative",
      new NonNegative {}
    )

  type NonNegativeDouble = Double Refined NonNegative


}
