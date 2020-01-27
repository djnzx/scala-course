package warmup

object FizzBuzzApp extends App {

  val div3 = (n: Int) => n % 3 == 0
  val div5 = (n: Int) => n % 5 == 0
  val div3a5 = (n: Int) => div3(n) && div5(n)

  val r = 1 to 100 map {
    case n if div3a5(n) => "FizzBuzz"
    case n if div3(n) => "Fizz"
    case n if div5(n) => "Buzz"
    case n => n.toString
  } reduce {
    (s1, s2) => s"$s1\n$s2"
  }
  println(r)
}
