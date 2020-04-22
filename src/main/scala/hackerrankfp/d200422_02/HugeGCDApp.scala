package hackerrankfp.d200422_02

object HugeGCDApp {
  type BD = java.math.BigDecimal
  def vbd(v: Vector[Int]): BD = v.foldLeft(new BD(1)) { (acc, el) => acc.multiply(new BD(el)) }
  def gcd(a: BD, b: BD): BD = {
    val r = a.remainder(b)
    if (r.compareTo(new BD(0))==0) b else gcd(b, r)
  }

  def main(args: Array[String]) {
    import scala.io.StdIn.readLine
    val _ = readLine
    val as: Vector[Int] = readLine.split(" ").map(_.toInt).toVector
    val _ = readLine
    val bs: Vector[Int] = readLine.split(" ").map(_.toInt).toVector
//    val bs = Vector(1,2,3,4,5,6)
//    val as = Vector(1,2,3,4,5)
    val ba = vbd(as)
    val bb = vbd(bs)
//    println(ba)
//    println(bb)
    val r = gcd(ba, bb).remainder(new BD(1_000_000_000+7))
    println(r)
  }
}
