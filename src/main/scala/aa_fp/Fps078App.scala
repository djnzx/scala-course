package aa_fp

import cats.effect.IO

object Fps078App extends App {

  val program: IO[Unit] = for {
    _      <- IO { println("Welcome to Scala!  What's your name?") }
    name   <- IO { scala.io.StdIn.readLine }
    nameUC = name.toUpperCase
    _      <- IO { println(s"Well hello, $nameUC!") }
  } yield ()

  program.unsafeRunSync()
}
