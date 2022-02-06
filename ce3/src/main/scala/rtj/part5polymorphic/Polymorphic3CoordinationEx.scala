package rtj.part5polymorphic

import cats.effect.Concurrent
import cats.effect.Fiber
import cats.effect.Outcome

object Polymorphic3CoordinationEx {

  import cats.syntax.flatMap._ // flatMap
  import cats.syntax.functor._ // map
  import cats.effect.syntax.spawn._ // start extension method
  import cats.effect.syntax.monadCancel._ // guaranteeCase extension method

  /** Exercises:
    *   1. Generalize racePair 2. Generalize the C3Mutex1 concurrency primitive for any F
    */
  type RaceResult[F[_], A, B] = Either[
    (Outcome[F, Throwable, A], Fiber[F, Throwable, B]), // (winner result, loser fiber)
    (Fiber[F, Throwable, A], Outcome[F, Throwable, B]), // (loser fiber, winner result)
  ]

  type EitherOutcome[F[_], A, B] = Either[Outcome[F, Throwable, A], Outcome[F, Throwable, B]]

  def ourRacePair[F[_], A, B](fa: F[A], fb: F[B])(implicit concurrent: Concurrent[F]): F[RaceResult[F, A, B]] =
    concurrent.uncancelable { poll =>
      for {
        signal <- concurrent.deferred[EitherOutcome[F, A, B]]
        fiba <- fa.guaranteeCase(outcomeA => signal.complete(Left(outcomeA)).void).start
        fibb <- fb.guaranteeCase(outcomeB => signal.complete(Right(outcomeB)).void).start
        result <- poll(signal.get).onCancel { // blocking call - should be cancelable
          for {
            cancelFibA <- fiba.cancel.start
            cancelFibB <- fibb.cancel.start
            _ <- cancelFibA.join
            _ <- cancelFibB.join
          } yield ()
        }
      } yield result match {
        case Left(outcomeA)  => Left((outcomeA, fibb))
        case Right(outcomeB) => Right((fiba, outcomeB))
      }
    }

}
