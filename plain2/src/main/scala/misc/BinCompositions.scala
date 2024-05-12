package b

object BinCompositions extends App {

  val variants: List[String] = List("0", "1")

  def comb(n: Int): List[String] = n match {
    case 0 => List("")
    case n => comb(n-1).flatMap(lx => variants.map(v => v + lx))
//    def combr(n: Int, acc: List[String]): List[String] = n match {
//      case 0 => List("")
//      case n =>
//        val x2: List[String] = variants.flatMap(pfx => acc.map(pfx + _))
//        combr(n - 1, x2)
//    }
//
//    combr(n, List(""))
  }

  comb(0)
    .foreach(println)

  println(Integer.toBinaryString(Int.MaxValue))
  println(Integer.toBinaryString(Int.MaxValue << 1))
  println(Int.MaxValue << 1)
}
