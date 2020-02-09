package aa_fp

object Fps071 extends App {

  val f = (a: Int) => { val r = a * 2; Loggable(r, s"f($a)=$r" :: Nil) }
  val g = (a: Int) => { val r = a * 3; Loggable(r, s"g($a)=$r" :: Nil) }
  val h = (a: Int) => { val r = a * 4; Loggable(r, s"h($a)=$r" :: Nil) }

  val r = for {
    fr <- f(100)
    gr <- g(fr)
    hr <- h(gr)
  } yield hr

  case class Loggable[A](value: A, log: List[String]) {
    def map[B](f: A => B): Loggable[B] = Loggable(f(value), log)
    def flatMap[B](f: A => Loggable[B]): Loggable[B] = {
      val next = f(value)
      Loggable(next.value, this.log ::: next.log)
    }
  }

  println(r.value)
  println(r.log.mkString(" >> "))

}
