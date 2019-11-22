package x00warmup

object Nth extends App {

  def fmt_1(n: Int) = n match {
    case 1 => "%d-st"
    case 2 => "%d-nd"
    case 3 => "%d-rd"
    case _ => "%d-th"
  }
  val fmt_2 = " number is:%d"
  val m = scala.io.StdIn.readLine("Enter the number:").toInt

  // Java Solution
  var mth_number = 0
  var number = 1
  var countdown = m // counter to count down
  while (countdown > 0) {
    if ((number % 3 == 0) || (number % 4 == 0)) {
      countdown -= 1
      mth_number = number
    }
    number += 1
  }
  print(m.formatted(fmt_1(m)))
  println(mth_number.formatted(fmt_2))

  // Scala Solution
  val from1 = LazyList from 1
  val mth_number_2 = from1
    .filter(n => (n % 3 == 0 ) || (n % 4 == 0)) // filter by rule
    .zip(from1)
    .filter(t => t._2 == m) // take n-th element
    .head
    ._1

  print(m.formatted(fmt_1(m)))
  println(mth_number_2.formatted(fmt_2))
}
