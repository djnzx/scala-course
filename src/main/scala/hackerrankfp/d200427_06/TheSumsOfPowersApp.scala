package hackerrankfp.d200427_06

/**
  * https://www.hackerrank.com/challenges/functional-programming-the-sums-of-powers/problem
  */
object TheSumsOfPowersApp {
  def pow(x: Int, n: Int): Int = (1 to n).foldLeft(1)((acc, _) => acc * x)

  def tryFrom(n: Int, p: Int, number: Int, subsum: Int, acc: List[Int]): Option[List[Int]] = {
    if (n==subsum) Some(acc.reverse)
    else if (n<subsum) None
    else if (number==0) None
    else tryFrom(n, p, number-1, subsum+pow(number, p), number::acc)
  }

  def process(n: Int, x: Int) = {

//    def tryFrom(m: Int, subSum: Int, acc: List[Int]): List[Option[List[Int]]] = {
//      //println(s"m=$m, sub=$subSum, acc=$acc")
//      if (n==subSum) List(Some(acc.reverse))
//      else if (m==0) List(None)
//      else {
//        Range.inclusive(m, 1, -1).foldLeft(List.empty[Option[List[Int]]]) { (acc, m1) =>
//          val mx = pow(m1, x)
//          val sum = subSum + mx
//          if (sum <= n) tryFrom(m-1, sum, m1::acc)
//          else          tryFrom(m-1, subSum, acc)
//        }
//      }
//    }
//    def tryFrom(list: List[Int], subSum: Int, partial: List[Int]): List[List[Int]] = {
//      if (n==subSum) List(partial)
//      else if (list.isEmpty || subSum>n) Nil
//      else list.tail.flatMap(z => tryFrom(list.tail, subSum+pow(z, x), z::partial))
//    }
//
    val max = math.ceil(math.sqrt(n)).toInt
//    val seq = Range.inclusive(max, 1, -1).toList
//    val r: List[List[Int]] = seq.flatMap(_ => tryFrom(seq, 0, Nil))
//    println(r)
//r

    //tryFrom(max, 0, Nil).flatten
//    Range.inclusive(max, 1, -1).foldLeft(List.empty[Option[List[Int]]]) { (acc, s) =>
//      println(s)
//      val r = tryFrom(s, 0, Nil)
//      println(r)
//      r :: acc
//    }.flatten.distinct
    tryFrom(n, x, max, 0, Nil)
  }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt
    val X: Int = readLine.toInt

    val r = process(N, X)//.size
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200427_06/thesumofp.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
