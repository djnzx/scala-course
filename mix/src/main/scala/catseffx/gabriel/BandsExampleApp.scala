package catseffx.gabriel

import java.io.File
import java.util.UUID

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration._

object BandsExampleApp extends IOApp {

  def fileFromResources(name: String) = new File(this.getClass.getClassLoader.getResource(name).getFile)

  case class Band(value: String)
  def putStrLn(str: String) = IO(println(str))

  def generateId: IO[UUID] = IO(UUID.randomUUID())
  def longProcess1(bands: List[Band]): IO[Unit] =
    putStrLn("StartingProcess 1") *> IO.sleep(3.seconds) *> putStrLn("Process 1 Done")
  def longProcess2(bands: List[Band]): IO[Unit] =
    putStrLn("StartingProcess 2") *> IO.sleep(2.seconds) *> putStrLn("Process 2 Done")
  def publishRadio(id: UUID, bands: List[Band]): IO[Unit] =
    putStrLn(s"Radio Chart for $id: ${bands.map(_.value).mkString(",")}")
  def publishTv(id: UUID, bands: List[Band]): IO[Unit] =
    putStrLn(s"Tv Chart for $id: ${bands.map(_.value).mkString(",")}")

  val generateChart: IO[Unit] =
    for {
      bs <- getBandsFromFile
      _  <- IO.race(longProcess1(bs), longProcess2(bs))
      id <- generateId
      _  <- publishRadio(id, bs)
      _  <- publishTv(id, bs)
    } yield ()

  def getBandsFromFile: IO[List[Band]] =
    IO {
      val url = this.getClass.getClassLoader.getResource("bands.txt")
      val file = new File(url.getFile)
      scala.io.Source.fromFile(file)
    }.flatMap { bs =>
      val bands = bs.getLines().toList.map(Band)
      IO.pure(bands) <* IO(bs.close())
    }

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- generateChart
  } yield ExitCode.Success
}
