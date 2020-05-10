package pfps

import cats.effect._
import cats.effect.concurrent.Semaphore
import cats.implicits._

import scala.concurrent.duration._

object A032RegionsOfSharing extends App {
  object SharedState extends IOApp {

    def putStrLn(str: String): IO[Unit] = IO(println(str))

    def someExpensiveTask: IO[Unit] = IO.sleep(1.second) >> putStrLn("expensive task") >> someExpensiveTask

    def p1(sem: Semaphore[IO]): IO[Unit] = sem.withPermit(someExpensiveTask) >> p1(sem)

    def p2(sem: Semaphore[IO]): IO[Unit] = sem.withPermit(someExpensiveTask) >> p2(sem)

    def run(args: List[String]): IO[ExitCode] = {
      val s: IO[Semaphore[IO]] = Semaphore[IO](1)

      // this guy can leak
      val s_leaked: Semaphore[IO] = Semaphore[IO](1).unsafeRunSync()

      // this is okay
      s.flatMap { sem =>
        p1(sem).start.void *> p2(sem).start.void
      } *> IO.never.as(ExitCode.Success) }
    }
}
