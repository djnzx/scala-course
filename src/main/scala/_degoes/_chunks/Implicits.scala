package _degoes._chunks

/**
  * https://docs.scala-lang.org/tutorials/FAQ/finding-implicits.html
  */
object Implicits extends App {
  object Y {
    implicit val n: Int = 2

    trait T {
      implicit val i: Int = 3
      implicit def t: T   = ???
    }

    object X extends T {
      implicit val n: Int = 4
      implicit val s: String = "hello, world\n"

      def f(implicit s: String) = implicitly[String] * implicitly[Int]

      override def t: T = ???

      /**
        * since we have overridden `implicit def t: T = ???`
        *                     with `override def t: T = ???` which isn't implicit
        * we can't write `def g = implicitly[T]`
        * this won't compile
        *
        */
      //      def g = implicitly[T]
    }
  }
  import Y.X._
  val z: String = f
  println(z)

}
