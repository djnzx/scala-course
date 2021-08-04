import zio._

object ZApp extends App {

  val x: UIO[Int] = ZIO.succeed(5)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = ???

}
