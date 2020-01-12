package _twitter

import scala.concurrent.Future
import scala.language.implicitConversions

/**
  * Scala Advanced Types
  * https://twitter.github.io/scala_school/advanced-types.html
  */
object TWApp03 extends App {
  implicit def strToInt(x: String) = x.toInt
  val max: Int = math.max("123", 111)

  /**
    * View bounds: deprecated
    * `A` has to be `viewable` as Int
    */
  class Container[A <% Int] { def addIt(x: A): Int = 123 + x }
  (new Container[String]).addIt("123")
  (new Container[Int]).addIt(123)
  // could not find implicit value for evidence parameter of type (Float) => Int
//  (new Container[Float]).addIt(123.2F)

  /**
    * Other type bounds
    *
    * def sum[B >: A](implicit num: Numeric[B]): B
    * that means B must implement Numeric[B]
    */
  val s: Int = List(1,2,3).sum

  /**
    * A =:= B --- A must be equal to B
    * A <:< B --- A must be a subtype of B
    * A <%< B --- A must be viewable as B
    */
  class ContainerA[A](value: A) {
    def addIt(implicit ev: A =:= Int) = 123 + value
  }

  // since scala 2.8 we can write
  def foo1[A](implicit ev: Ordered[A]) {}
  def foo2[A: Ordered] {} // means `A` implements `Ordered`
  val o: Ordering[Int] = implicitly[Ordering[Int]]

  trait ContainerHK[M[_]] {
    def put[A](x: A): M[A]
    def get[A](m: M[A]): A
  }

  new ContainerHK[List] {
    override def put[A](x: A): List[A] = ???
    override def get[A](m: List[A]): A = ???
  }

  new ContainerHK[Option] {
    override def put[A](x: A): Option[A] = ???
    override def get[A](m: Option[A]): A = ???
  }

  new ContainerHK[Future] {
    override def put[A](x: A): Future[A] = ???
    override def get[A](m: Future[A]): A = ???
  }
}
