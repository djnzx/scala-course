package orsyntax

object OrSyntaxApp extends App {

  // this is initial idea without involving Applicative
  trait Alternative[F[_]] {
    def isEmpty(a: F[_]): Boolean
    def choose[A](me: F[A], another: F[A]): F[A] = if (isEmpty(me)) another else me
  }

  implicit val altOption: Alternative[Option] = (a: Option[_]) => a.isEmpty

  implicit class AlternativeOps[F[_], A](x: F[A]) {
    def |(y: F[A])(implicit ev: Alternative[F]): F[A] = ev.choose(x, y)
  }

//  implicit class OptionOps[A](oa: Option[A]) {
//    def |(oa2: Option[A]): Option[A] = oa match {
//      case None => oa2
//      case _    => oa
//    }
//  }

  implicit val AltList: Alternative[List] = (a: List[_]) => a.isEmpty

  val x = Option(5) | Some(6)   // Some(5)
  val y = None | Some(7)        // Some(7)
  val z = None | None | Some(3) // Some(3)
  val k = None | None           // None

  List(x, y, z, k).foreach(x => pprint.pprintln(x))

  implicit class EitherOps[E, A](ea: Either[E, A]) {
    def |(ea2: Either[E, A]): Either[E, A] = ea match {
      case Left(_) => ea2
      case _       => ea
    }
  }

  val a = Right(3) | Right(4)               // Right(3)
  val b = Left("boom!") | Right(1)          // Right(1)
  val c = Left("a") | Left("b") | Right(13) // Right(13)
  val d = Left("a") | Left("b")             // Left("b")

  List(a, b, c, d).foreach((x: Either[String, Int]) => pprint.pprintln(x))

  pprint.pprintln(List() | List(1, 2, 3))  // List(1, 2, 3)
  pprint.pprintln(List(1) | List(1, 2, 3)) // List(1)

}
