package cats.kleisli_flatmap

object Step1 extends App {

  // we can combine steps by using flatMap:
  val combined: Int => List[Int] = (x: Int) => for {
    a <- f1(x)
    b <- f2(a)
    c <- f3(b)
  } yield c

  // we run our function
  val r = combined(10) // List(22, 5, -22, -5, 18, 4, -18, -4)
  // but we need to declare params
}
