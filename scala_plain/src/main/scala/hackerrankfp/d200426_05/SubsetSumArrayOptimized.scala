package hackerrankfp.d200426_05

/**
  * https://www.hackerrank.com/challenges/subset-sum/problem
  */
object SubsetSumArrayOptimized {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  def process1(wholeSum: Long, data: List[Int], n: Long): Int = if (wholeSum < n) 0 else {

    case class SumCnt(sum: Long, cnt: Int)

    // optimized fold
    @scala.annotation.tailrec
    def foldLazy(tail: List[Int], acc: SumCnt): SumCnt = tail match {
      case Nil => acc
      case h::t => if (acc.sum >= n) acc else foldLazy(t, SumCnt(acc.sum + h, acc.cnt+1))
    }
    val folded: SumCnt = foldLazy(data, SumCnt(0L, 0))
    if (folded.sum >= n) folded.cnt else 0
  }

  type TLI = (Long, Int)
  type TII = (Int, Int)

  def process(data: List[Int],
//              testsR: List[Long]
              testsR: List[TLI]
             ) = {
    val totalL = data.foldLeft(0L)(_+_)
//    @scala.annotation.tailrec
//    def go(tail: List[TLI], keep: Boolean, acc: List[TII]): List[TII] = {
//      //      println(keep)
//      (keep, tail) match {
//        case (_,     Nil)  => acc.reverse
//        case (false, h::t) => go(t, keep = false, (0, h._2)::acc)
//        case (true,  h::t) => {
//          val count = process1(totalL, data, h._1)
//          go(t,
//            //            true
//            count != 0
//            , (count, h._2)::acc)
//        }
//      }
//    }
    testsR
//      .sorted { Ordering.Long.on { t: TLI => t._1 } } // by data


//    val tB: List[(Int, Int)] = go(tA, keep = true, Nil)
//    println(s"COUNT=${counter}")
//    println(System.currentTimeMillis()-start)
//      (
//        tA
//        .map { t => process1(totalL, data, t) }
        .map { t => (process1(totalL, data, t._1), t._2) }
//    (
//          tB
//        .sorted { Ordering.Int.on { t: TII => t._2 } } // by position
//          .sorted { Ordering.Int.on { t: TLI => t._2 } } // by position
//      .map { _._1 }
//      .map { l => if (l>0) l else -1 }
//      .mkString("\n")
//        , counter)
  }

  def body(readLine: => String): Unit = {
    val _ = readLine
    val data = readLine.toListInt
    val N = readLine.toInt

    def readOneLine = readLine.split(" ").map(_.toLong).apply(0)
    def readNLines(n: Int, acc: List[Long]): List[Long] = n match {
      case 0 => acc.reverse
      case _ => readNLines(n-1, readOneLine::acc)
    }

    val r = process(
      data.sorted { (a:Int, b:Int) => b-a }, // dataset descending
      readNLines(N, Nil).zipWithIndex
    )
//    println(r)
  }

  val start = System.currentTimeMillis();

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
    println(s"time: ${System.currentTimeMillis()-start}")
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/subset100K.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
