package degoes.fp_crawler

import java.io.IOException

import zio.ZIO
import zio.console.{Console, getStrLn, putStr, putStrLn}

object ZIOMinimalApp extends App {

    def run(args: List[String]): ZIO[Console, Nothing, Int] = {

      val app: ZIO[Console, IOException, Unit] = for {
        _ <- putStr("Enter your name:")   // ZIO[Console, Nothing, Unit]
        s <- getStrLn                           // ZIO[Console, IOException, String]
        _ <- putStrLn(s"Hello, $s")       // ZIO[Console, Nothing, Unit]
      } yield ()                                // ZIO[Console, IOException, Unit]

      app.fold(_ => 1, _ => 0)// (CanFail)
    }

}
