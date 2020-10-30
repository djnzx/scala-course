package fp_crawler

import java.io.IOException

import zio.{ExitCode, URIO, ZIO, App}
import zio.console.{Console, getStrLn, putStr, putStrLn}

object ZIOMinimalApp extends App {

  val app: ZIO[Console, IOException, Unit] = for {
    _ <- putStr("Enter your name:")   // ZIO[Console, Nothing, Unit]
    s <- getStrLn                          // ZIO[Console, IOException, String]
    _ <- putStrLn(s"Hello, $s")       // ZIO[Console, Nothing, Unit]
  } yield ()                               // ZIO[Console, IOException, Unit]
  
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
