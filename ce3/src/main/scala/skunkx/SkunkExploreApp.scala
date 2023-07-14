package skunkx

import cats.Show
import cats.effect._
import natchez.Trace.Implicits.noop
import skunk._

trait SkunkExploreApp extends IOApp.Simple {

  // docker run -p5432:5432 -d tpolecat/skunk-world
  val sessionR: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 5432,
      database = "world",
      user = "jimmy",
      password = Some("banana")
    )

  val app: IO[Unit]

  override def run: IO[Unit] = app

  def printlnF[A: Show](a: A) = IO.println(a)

  def pprintln[A](a: A)  = pprint.pprintln(a)
  def pprintlnF[A](a: A) = IO.blocking(pprintln(a))
  val dividerF           = printlnF("-" * 50)

}
