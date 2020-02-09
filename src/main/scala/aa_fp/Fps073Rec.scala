package aa_fp

import scala.util.{Success, Try}

object Fps073Rec extends App {

  val print: String => XIO[Unit] = (line: String) => Try(line.toInt) match {
    case Success(value) => putLn(s"number $value entered")
    case _ => putLn("non number entered")
  }

  val print_and_loop: String => XIO[Unit] = (s: String) => for {
    _ <- print(s)
    _ <- app_loop
  } yield ()

  val app_finish: XIO[Unit] = for {
    _ <- putLn("Bye!")
    _ <- quit
  } yield ()

  val quit_or_loop: String => XIO[Unit] = {
    case "quit" => app_finish
    case s => print_and_loop(s)
  }

  val app_loop: XIO[Unit] = for {
    _ <- putLn("Enter number")
    s <- getLn
    _ <- quit_or_loop(s)
  } yield ()

  val app = for {
    _ <- putLn("Hi")
    _ <- app_loop
  } yield ()

  app.run
}
