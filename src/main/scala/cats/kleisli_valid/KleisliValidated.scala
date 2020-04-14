package cats.kleisli_valid

import cats.C210UseCase10Recap.Errors
import cats.data.{Kleisli, Validated}

object KleisliValidated extends App {
  type CheckFn[E, A] = A => Validated[E, A]
  // but we need A => F[B]
  type Result[A] = Validated[Errors, A]
  // so, we have A => Result[B]
  type Check[A, B] = Kleisli[Result, A, B]


}
