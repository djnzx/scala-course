import zio._

/**
  * 2. https://www.youtube.com/watch?v=7MNY0o2i_vg
  * 3. https://www.youtube.com/watch?v=oKaQXxEa7a8 // Variance cool explanation
  * 4. https://www.youtube.com/watch?v=e1kIjiWHVhE
  * 5. https://www.youtube.com/watch?v=1t0GPFumFkE
  * 6. https://www.youtube.com/watch?v=epTKGRuxbOE
  * 7. https://www.youtube.com/watch?v=FOStEDZAZWs
  * 8. https://www.youtube.com/watch?v=WjjdlhvOS7Y
  * -XSource:3
  * -Ysafe-init
  */

object Z1App extends App {

  val trace = s"[${scala.Console.BLUE}TRACE${Console.RESET}]"

  val z5 = ZIO.succeed(5)

  val program = for {
    _ <- badConsole.putStrLn("-" * 50)
    _ <- badConsole.putStrLn("What is your name?")
    name <- badConsole.getStrLn
    _ <- badConsole.putStrLn(s"Hi, $name!")
    _ <- badConsole.putStrLn("-" * 50).debug(trace)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program.exitCode

}
