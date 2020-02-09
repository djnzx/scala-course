package aa_fp

object Fps070Gen extends App {

  case class Debug[A](value: A, msg: String) { self =>

    def map[B](f: A => B): Debug[B] = {
      val next: B = f(self.value)
      Debug(next, self.msg)
    }

    def flatMap[B](f: A => Debug[B]): Debug[B] = {
      val next: Debug[B] = f(self.value)
      Debug(next.value, s"${self.msg} >> ${next.msg}")
    }
  }

  val f = (a: Int) => { val r = a * 2; Debug(r, s"f($a)=$r") }
  val g = (a: Int) => { val r = a * 3; Debug(r, s"g($a)=$r") }
  val h = (a: Int) => { val r = a * 4; Debug(r, s"h($a)=$r") }

  val r = for {
    fr <- f(100)
    gr <- g(fr)
    hr <- h(gr)
  } yield hr

  println(r.value)
  println(r.msg)
}
