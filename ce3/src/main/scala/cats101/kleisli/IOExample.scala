package cats101.kleisli

import cats.data.Kleisli
import cats.effect.IO
import cats.implicits.toComposeOps

/**
  * https://blog.softwaremill.com/kleisli-category-from-theory-to-cats-fbd140bf396e
  */

object IOExample extends App {
  val r = scala.util.Random

  /** our implementation */
  val generate: Unit => IO[Int] = _ => IO.pure(r.nextInt(100))
  val process: Int => IO[Double] = num => IO.pure(num * math.Pi)
  val save: Double => IO[Boolean] = _ => IO.pure(true)

  /** syntax w.o Kleisli #1 */
  val combined1: () => IO[Boolean] = () => {
    generate(()).flatMap { number =>
      process(number).flatMap { processed =>
        save(processed)
      }
    }
  }

  /** syntax w.o Kleisli #2 */
  val combined2 = (input: Unit) => for {
    a <- generate(input)
    p <- process(a)
    z <- save(p)
  } yield z

  
  /** syntax having Kleisli #1 */
  val combined3: Kleisli[IO, Unit, Boolean] = {
    val generateK:Kleisli[IO, Unit, Int] = Kleisli(generate)
    val processK:Kleisli[IO, Int, Double] = Kleisli(process)
    val saveK:Kleisli[IO, Double, Boolean] = Kleisli(save)
    generateK andThen processK andThen saveK
  }

  /** syntax having Kleisli #2 */
  val combined4: Kleisli[IO, Unit, Boolean] =
    Kleisli(generate) andThen Kleisli(process) andThen Kleisli(save)

  /** syntax having Kleisli #3 */
  val combined5: Kleisli[IO, Unit, Boolean] =
    Kleisli(generate) >>> Kleisli(process) >>> Kleisli(save)

  /** syntax having Kleisli #4 */
  val combined6: Kleisli[IO, Unit, Boolean] =
    Kleisli(generate) andThen process andThen save
  
}
