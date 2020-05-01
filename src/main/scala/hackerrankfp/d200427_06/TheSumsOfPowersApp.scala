package hackerrankfp.d200427_06

/**
  * https://www.hackerrank.com/challenges/functional-programming-the-sums-of-powers/problem
  */
object TheSumsOfPowersApp {

  def pow(x: Int, n: Int): Int = (1 to n).foldLeft(1)((acc, _) => acc * x)

  def tryFromL(n: Int, p: Int, options: List[Int], subsum: Int, acc: List[Int]): List[Option[List[Int]]] = {
    println(s"N=$n, X=$p, from=$options, sub=$subsum, acc=$acc")
    if (n==subsum) List(Some(acc.reverse))
    else if (n<subsum) List(None)
    else if (options.isEmpty) List(None)
    else options match {
      case h::t => {
        val pw = pow(h, p)
        if (subsum + pw > n) tryFromL(n, p, t, subsum   ,    acc)
        else                 tryFromL(n, p, t, subsum+pw, h::acc)
      }
    }
  }

  def process(n: Int, x: Int) = {
    val max = math.ceil(math.sqrt(n)).toInt
    val nums = (1 to max+1).reverse.toList

    type LOLI = List[Option[List[Int]]]
    def proc(opts: List[Int], acc: List[Option[List[Int]]]): List[Option[List[Int]]] = opts match {
      case Nil => acc
      case _::t => proc(t, tryFromL(n, x, t, 0, Nil):::acc)
    }

    val r: List[List[Int]] = proc(nums, Nil).flatten
    println("--")
    r
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
