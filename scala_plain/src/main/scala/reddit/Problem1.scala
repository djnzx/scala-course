package reddit

object Problem1 extends App {

  case class Wrapper[A](a: A, fun: A => String)

  val w1: Wrapper[String] = Wrapper("String", (x: String) => x)
  val w2: Wrapper[Int] = Wrapper(1, (x: Int) => x.toString)
  val seq: Seq[Wrapper[_ >: String with Int]] = Seq(w1, w2)
  val seq1: Seq[Wrapper[_]] = Seq(w1, w2)

//  seq.foreach { w: Wrapper[_] =>
//    w.fun(w.a)
//  }

//  Seq(
//    Wrapper("String", (x: String) => x),
//    Wrapper(1, (x: Int) => x.toString)
//  ).foreach{ w =>
//    w.fun(w.a)
//  }

  trait Wrapper2[A] {
    val a: A
    def fun(a: A): String
  }
  val s: Wrapper2[String] = new Wrapper2[String] {
    val a: String = "String"
    def fun(a: String): String = a
  }
  val i: Wrapper2[Int] = new Wrapper2[Int] {
    val a = 1
    def fun(a: Int): String = a.toString
  }
  val sq: Seq[Wrapper2[_ >: String with Int]] = Seq(s, i)
//  sq.foreach { w: Wrapper2[_ >: String with Int] =>
//    w.fun(w.a)
//  }

//    .foreach{ w =>
//    println(w.fun(w.a))
//  }
}
