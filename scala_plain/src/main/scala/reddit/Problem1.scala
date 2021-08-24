package reddit

object Problem1 extends App {

  case class Wrapper[A](a: A, fun: A => String)

  val w1: Wrapper[String] = Wrapper("String", (x: String) => x)
  val w2: Wrapper[Int] = Wrapper(1, (x: Int) => x.toString)
  val seq: Seq[Wrapper[_ >: String with Int]] = Seq(w1, w2)

//  Seq(
//    Wrapper("String", (x: String) => x),
//    Wrapper(1, (x: Int) => x.toString)
//  ).foreach{ w =>
//    w.fun(w.a)
//  }

}
