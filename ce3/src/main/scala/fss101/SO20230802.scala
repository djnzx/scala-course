package fs2x

import cats.effect.IO
import cats.implicits._
import fs2._
import fs2.io.file.Files
import fs2.io.file.Path
import java.nio.file.{Path => JPath}

// https://stackoverflow.com/questions/76817051/handle-large-file-with-stream-in-scala/76819409#76819409
object SO20230802 extends App {

  case class User(a: Any, b: Any, c: Any, d: Any)

  val givenUser: User = ???

  def readUsers(filePath: JPath): IO[Boolean] =
    Files[IO]
      .readAll(Path.fromNioPath(filePath))
      .through(text.utf8.decode)
      .through(text.lines)
      .map(_.split(","))
      .map {
        case Array(a, b, c, d) => User(a, b, c, d).some
        case _                 => None
      }
      .unNone
      .dropWhile(_ != givenUser)
      .take(1)
      .as(true)
      .lastOr(false)
      .compile
      .lastOrError

}
