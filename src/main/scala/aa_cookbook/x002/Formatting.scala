package aa_cookbook.x002

import java.text.NumberFormat
import java.util.{Currency, Locale}

object Formatting extends App {
  val pi = math.Pi
  println(f"$pi%1.5f")
  println(f"$pi%10.5f")
  println(f"$pi%010.5f")

  val lc = new Locale("us", "US")
  val fmt = NumberFormat.getInstance(lc)
  val us = Currency.getInstance(lc)
  println(us)
  println(fmt.format(1000))
  fmt.setCurrency(us)
  println(fmt.format(1000))
}
