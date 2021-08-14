import zio._

object badConsole {
  def putStrLn(line: => String) = ZIO.succeed(println(line))
  def getStrLn = ZIO.succeed(scala.io.StdIn.readLine())
}
