package catsx.kleisli_basics

import cats.data.Kleisli
import cats.effect.IO
import cats.implicits._

/**
  * https://blog.softwaremill.com/kleisli-category-from-theory-to-cats-fbd140bf396e
  */

object Step2 extends App {
  val r = scala.util.Random

  val generate: Unit => IO[Int] = _ => IO.pure(r.nextInt(100))
  val process: Int => IO[Double] = num => IO.pure(num * math.Pi)
  val save: Double => IO[Boolean] = _ => IO.pure(true)

  val comboFlatMap: () => IO[Boolean] = () => {
    generate(()).flatMap { number =>
      process(number).flatMap { processed =>
        save(processed)
      }
    }
  }
  println(s"FlatMap that sh**t: ${comboFlatMap().unsafeRunSync()}")

  val effect = for {
    a <- generate(())
    p <- process(a)
    z <- save(p)
  } yield z
  println(s"For comprehension version: ${effect.unsafeRunSync()}")

  val kleisliCombine_1: Kleisli[IO, Unit, Boolean] = {
    val generateK:Kleisli[IO, Unit, Int] = Kleisli(generate)
    val processK:Kleisli[IO, Int, Double] = Kleisli(process)
    val saveK:Kleisli[IO, Double, Boolean] = Kleisli(save)
    generateK andThen processK andThen saveK
  }
  println(s"Kleilis example 1: ${kleisliCombine_1.run(()).unsafeRunSync()}")

  val kleisliCombine_2: Kleisli[IO, Unit, Boolean] = Kleisli(generate) >>> Kleisli(process) >>> Kleisli(save)
  println(s"Kleilis example 2: ${kleisliCombine_2.run(()).unsafeRunSync()}")

  val kleisliCombine_3: Kleisli[IO, Unit, Boolean] = Kleisli(generate) andThen process andThen save
  println(s"Kleilis example 3: ${kleisliCombine_3.run(()).unsafeRunSync()}")
}
