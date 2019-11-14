package x060essential

object X190Divide extends App {

  def wrap(n: Int): Option[Int] = if (n ==0) None else Some(n)

  def divide(a: Int, b: Int): Either[String, Int] = {
    val aa = wrap(a)
    val bb = wrap(b)
    val r: Option[Int] = for {
      a1 <- aa
      b2 <- bb
      c <- Some(a1/b2)
    } yield c
    r.toRight("You aren't allowed to divide by zero")
  }

  println(divide(20,0))


}
