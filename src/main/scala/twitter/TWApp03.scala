package twitter

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

  val c: ContainerHK[List] = new ContainerHK[List] {
    override def put[A](x: A): List[A] = List(x) // put 1 el
    override def get[A](m: List[A]): A = m.head  // get 1 el
  }

  val cx = new ContainerHK[Some] {
    override def put[A](x: A): Some[A] = Some(x)
    override def get[A](m: Some[A]): A = m.get
  }

  val l1 = c.put(123)
  val l2 = c.put("abc")
  println(l1)
  println(l2)
  val x1: Int = c.get(l1)
  val x2: String = c.get(l2)
  println(x1)
  println(x2)

  def tupleize[M[_]: ContainerHK, A, B](fst: M[A], snd: M[B]): M[(A, B)] = {
    val c: ContainerHK[M] = implicitly[ContainerHK[M]]
    c.put((c.get(fst), c.get(snd)))
  }

}
