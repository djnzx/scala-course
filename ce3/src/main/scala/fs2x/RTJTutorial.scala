package fs2x

import cats.effect.{IO, IOApp}
import fs2.{Chunk, Pipe, Pure, Stream}

import scala.concurrent.duration.DurationInt

object RTJTutorial extends IOApp.Simple {

  object Model {
    case class Actor(id: Int, firstName: String, lastName: String)
  }

  object Data {
    import Model._
    // Justice League
    val henryCavil: Actor = Actor(0, "Henry", "Cavill")
    val galGodot: Actor = Actor(1, "Gal", "Godot")
    val ezraMiller: Actor = Actor(2, "Ezra", "Miller")
    val benFisher: Actor = Actor(3, "Ben", "Fisher")
    val rayHardy: Actor = Actor(4, "Ray", "Hardy")
    val jasonMomoa: Actor = Actor(5, "Jason", "Momoa")

    // Avengers
    val scarlettJohansson: Actor = Actor(6, "Scarlett", "Johansson")
    val robertDowneyJr: Actor = Actor(7, "Robert", "Downey Jr.")
    val chrisEvans: Actor = Actor(8, "Chris", "Evans")
    val markRuffalo: Actor = Actor(9, "Mark", "Ruffalo")
    val chrisHemsworth: Actor = Actor(10, "Chris", "Hemsworth")
    val jeremyRenner: Actor = Actor(11, "Jeremy", "Renner")
    val tomHolland: Actor = Actor(13, "Tom", "Holland")
    val tobeyMaguire: Actor = Actor(14, "Tobey", "Maguire")
    val andrewGarfield: Actor = Actor(15, "Andrew", "Garfield")
  }

  import Data._
  import Model._
  val s1: Stream[Pure, Actor] = Stream(
    henryCavil,
    galGodot,
    ezraMiller,
    benFisher,
    rayHardy,
    jasonMomoa,
  )
  val s2: Stream[Pure, Actor] = Stream.emit(henryCavil)
  val s3: Stream[Pure, Actor] = Stream.emits(Seq(henryCavil, galGodot))

  // plain collect (works only for Stream[Pure, _])
  val r1: List[Actor] = s1.toList
  val r2: Vector[Actor] = s1.toVector
  val s4: Stream[Pure, Actor] = s1.repeat.drop(5).take(1)

  // eval 1
  val es1: Stream[IO, Actor] = Stream.eval(IO { henryCavil })
  // eval Seq
  val es2: Stream[IO, Actor] = Stream.evalSeq(
    IO(
      Seq(henryCavil, galGodot),
    ),
  )
  // eval Foldable
  val es3: Stream[IO, Actor] = Stream.evals(
    IO(
      List(henryCavil, galGodot),
    ),
  )

  // eval & drain
  val re1: IO[Unit] = es3.compile.drain
  // eval & get values
  val re2: IO[List[Actor]] = es3.compile.toList

  val s5: Stream[Pure, Actor] = Stream.chunk(
    Chunk.array(
      Array(
        scarlettJohansson,
        robertDowneyJr,
        chrisEvans,
        markRuffalo,
        chrisHemsworth,
        jeremyRenner,
        tomHolland,
        tobeyMaguire,
        andrewGarfield,
      ),
    ),
  )

  val s7: Stream[IO, Unit] = s1.evalMap(IO.println)

  val pipe: Pipe[IO, String, Option[Double]] = si => si.map(_.toDoubleOption)

  /** */
  case class DatabaseConnection(connection: String) extends AnyVal

  val acquire: IO[DatabaseConnection] = IO {
    val conn = DatabaseConnection("jlaConnection")
    println(s"Acquiring connection to the database: $conn")
    conn
  }

  val release: DatabaseConnection => IO[Unit] = (conn: DatabaseConnection) =>
    IO.println(s"Releasing connection to the database: $conn")

  val savedJlActors: Stream[IO, Int] = s1.evalMap(a => IO.println(s"saving...$a") >> IO.sleep(2.seconds).as(a.id))

  val s8: Stream[IO, Int] = Stream.bracket(acquire)(release).flatMap(conn => savedJlActors)

  override def run: IO[Unit] = IO.unit
}
