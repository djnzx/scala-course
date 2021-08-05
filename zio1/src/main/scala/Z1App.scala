import zio._

/**
  * 2. https://www.youtube.com/watch?v=7MNY0o2i_vg
  * 3. https://www.youtube.com/watch?v=oKaQXxEa7a8
  * 4. https://www.youtube.com/watch?v=e1kIjiWHVhE
  * 5. https://www.youtube.com/watch?v=1t0GPFumFkE
  * 6. https://www.youtube.com/watch?v=epTKGRuxbOE
  * 7. https://www.youtube.com/watch?v=FOStEDZAZWs
  * -XSource:3
  * -Ysafe-init
  */

object ourConsole {
  def putStrLn(line: => String) = ZIO.succeed(println(line))
  def getStrLn = ZIO.succeed(scala.io.StdIn.readLine())
}

object Z1App extends App {

  val trace = s"[${scala.Console.BLUE}TRACE${Console.RESET}]"

  val z5 = ZIO.succeed(5)

  val program = for {
    _ <- ourConsole.putStrLn("-" * 50)
    _ <- ourConsole.putStrLn("What is your name?")
    name <- ourConsole.getStrLn
    _ <- ourConsole.putStrLn(s"Hi, $name!")
    _ <- ourConsole.putStrLn("-" * 50).debug(trace)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program.exitCode

}
