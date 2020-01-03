package _degoes.hkt1

class HKTApp1 extends App {

  trait Console[F[_]] {
    def putStrLn(line: String): F[Unit]
    val getStrLn: F[String]
  }

  def consoleProgram[F[_]: Console]: F[Unit] =
    implicitly[Console[F]].putStrLn("Hello World!")

}
