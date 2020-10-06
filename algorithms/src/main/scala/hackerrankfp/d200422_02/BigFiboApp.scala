package hackerrankfp.d200422_02

object BigFiboApp {

  def fibo(n1: Int, n2: Int, c: Int): Int = c match {
    case 0 => 0
    case 1 => n2
    case _ => fibo(n2, n1+n2, c-1)
  }
  def fibo(c: Int): Int = fibo(0,1, c)


  def main(args: Array[String]) {
//    import scala.io.StdIn.readLine
//    val n = readLine().toInt
//    val fibos: Seq[(Int, Int)] = (1 to n) map { _ => readLine.toInt } zipWithIndex

    println(fibo(0))
    println(fibo(1))
    println(fibo(2))
    println(fibo(3))
    println(fibo(4))
    println(fibo(5))
  }
}
