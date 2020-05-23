package cookbook.x005

//import shapeless.syntax.std.tuple._

object Tuples extends App {
  class TupleEx {
    def make1(): Tuple3[Int, String, Boolean] = {
      (1, "name", true)
    }
    def make2(): (Int, String, Boolean) = {
      (1, "name", true)
    }
    def make3() = {
      (1, "name", true)
    }
  }

  val tx = new TupleEx
  val t1 = tx.make1()
  val t3 = tx.make2()
  val t4 = tx.make3()
  val t2 = (1, "name", true)
  println(t1 == t2)
  println(t3 == t2)
  println(t4 == t2)
  println(t1._1, t1._2, t1._3)
  val t5 = t1 -> 73

  implicit class Tuple3Ops[A, B, C](t: (A, B, C)) {
    def :+[T](v: T) = (t._1, t._2, t._3, v)
  }

  val t51 = t1 :+ 73
  println(t5)
  println(t51)
}
