import zio._

/**
  * https://www.youtube.com/watch?v=7MNY0o2i_vg
  */
object Z1App extends App {

  val trace = s"[${scala.Console.BLUE}TRACE${Console.RESET}]"

  val program = for {
    _ <- console.putStrLn("hello").debug(trace)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program.exitCode

}
