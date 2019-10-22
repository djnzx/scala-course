package _implicits.x1

object TypePattern extends App {

  // just trait
  trait Squash[T] {
    def squash_(a: T, b: T): T
  }
  // concrete implementation for Int
  implicit val squashInt: Squash[Int] = new Squash[Int] {
    override def squash_(a: Int, b: Int): Int = a + b
  }
  // concrete implementation for String
  implicit val squashStr: Squash[String] = new Squash[String] {
    override def squash_(a: String, b: String): String = a + b
  }
  // concrete implementation for Option[T]
  implicit def squashOpt[U](implicit impl: Squash[U]) = new Squash[Option[U]] {
    override def squash_(a: Option[U], b: Option[U]): Option[U] = for {
      v1 <- a
      v2 <- b
    } yield impl.squash_(v1, v2)
  }

  // #1 way to use:
  // we just explicitly say that Scala should find the
  // implicit implementation for specified type
  val s1: Int =            implicitly[Squash[Int]].squash_(1, 2)
  val s2: String =         implicitly[Squash[String]].squash_("1", "2")
  val s3: Option[Int] =    implicitly[Squash[Option[Int]]].squash_(Some(1), Some(3))
  val s4: Option[String] = implicitly[Squash[Option[String]]].squash_(Some("1"), Some("3"))

  // #2 way to use:
  // use scala functionality
  // we wrap explicit call into function with generic
  // generic resolution works well by inferring parameters type
  // and propagating them into generic to choose appropriate implementation
  def sq__[A: Squash](a: A, b: A): A = implicitly[Squash[A]].squash_(a, b)
  val q1: Int = sq__(1, 2)
  val q2: String = sq__("1", "2")
  val q3: Option[Int] = sq__(Option(1), Option(2))
  val q4: Option[String] = sq__(Option("1"), Option("2"))

  // #3 way to use, (smartest):
  // to make our life easier we can define an implicit class
  // with extra behavior, which will be automatically added to any T type
  // it expects implicit implementation for particular type
  implicit class Squashify[T](value: T)(implicit impl: Squash[T]) {
    def ^^^(another: T): T = impl.squash_(value, another)
  }
  // right now we can use 'squash' with all types for which we have declared
  // appropriate implicit values: 'squashInt' and 'squashStr'
  val z31: Int = 1 ^^^ 2
  val z32: String = "1" ^^^ "2"
  val z33: Option[Int] = Option(1) ^^^ Option(2)
  val z34: Option[String] = Option("1") ^^^ Option("2")

  // #4 way to use,
  // we can pass through the object generic type which we want to use explicitly
  object Squash {
    def apply[T](implicit impl: Squash[T]): Squash[T] = impl
  }
  val x1: Int            = Squash[Int].squash_(1, 2)
  val x2: String         = Squash[String].squash_("1", "2")
  val x3: Option[Int]    = Squash[Option[Int]].squash_(Some(1), Some(3))
  val x4: Option[String] = Squash[Option[String]].squash_(Some("1"), Some("3"))

  val instance1: Squash[Int]            = Squash[Int]
  val instance2: Squash[String]         = Squash[String]
  val instance3: Squash[Option[Int]]    = Squash[Option[Int]]
  val instance4: Squash[Option[String]] = Squash[Option[String]]

}
