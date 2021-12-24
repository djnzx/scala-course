package rtj

import cats.data.OptionT

object Transformers extends App {

  val ints = List(Option(1), Option(2), Option.empty[Int])
  val names = List(Option("Jim"), Option("Sergio"), Option("Nate"), Option.empty[String])

  val intsT: OptionT[List, Int] = OptionT(ints)
  val namesT: OptionT[List, String] = OptionT(names)

  val comb: OptionT[List, (Int, String)] = for {
    i <- intsT
    n <- namesT
  } yield (i, n)

  val combUnwrapped: List[Option[(Int, String)]] = comb.value

  pprint.pprintln(comb.value)

}
