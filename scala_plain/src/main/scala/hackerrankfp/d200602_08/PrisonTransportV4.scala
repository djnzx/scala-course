package hackerrankfp.d200602_08

/**
  * https://www.hackerrank.com/challenges/prison-transport/problem
  * quick, but wrong with 4 tests
  */
object PrisonTransportV4 {
  def priceOneGroup(n: Int): Int = math.ceil(math.sqrt(n)).toInt
  def priceAllGroups(xs: Iterable[Int]): Int = xs.toVector.map { priceOneGroup }.sum
  def priceTotal(N: Int, xs: Iterable[Int]): Int = N - xs.sum + priceAllGroups(xs)
  
  import scala.collection.mutable.ArrayBuffer
  case class Counter(N: Int) {
    private val id_g    = ArrayBuffer.fill[Int](N+1)(0)   // id    -> group mapping 
    private val g_cnt   = ArrayBuffer.fill[Int](N+1)(0)   // group -> count
    private val members = ArrayBuffer.fill[List[Int]](N+1)(Nil) // group -> members
    
    def groups = g_cnt.filter(_ > 0)
    def print = {
      println(s"id_g: $id_g")
      println(s"g_cnt: $g_cnt")
      println(s"members: $members")
    }
    def count(a: Int, b: Int): Unit = (id_g(a), id_g(b)) match {
      case (0, 0) =>
        val gid = a
        id_g(a) = gid
        id_g(b) = gid
        g_cnt(gid) = 2
        members(gid) = List(a,b)

      case (ga, 0) =>
        id_g(b) = ga               // tie id->gid
        g_cnt(ga) += 1             // inc counter
        members(ga) ::= b          // add to set

      case (0, gb) =>
        id_g(a) = gb               // tie id->gid
        g_cnt(gb) += 1             // inc counter
        members(gb) ::= a          // add to set

      case (ga, gb) =>
        g_cnt(ga) += g_cnt(gb)     // add to GA counter
        g_cnt(gb) = 0              // clear  GB counter
        val seta = members(ga)     // a-members
        val setb = members(gb)     // b-members
        setb.foreach(id_g(_) = ga) // rewrite group number for GB
        members(ga) = seta:::setb
        members(gb) = Nil
    }
  }
  
  def process(n: Int, pairs: Seq[(Int, Int)]) = {
    val c = Counter(n)
    pairs.foreach { case (a, b) => c.count(a, b) }
    priceTotal(n, c.groups)
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    val nPairs = line.toInt
    val pairs = (1 to nPairs).map { _ => line }.map { _.splitToInt match { case Array(a, b) => (a, b) }}
    val r = process(N, pairs)
    println(r)
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("prison3.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    if (!file.exists()) throw new RuntimeException("file not found")
    scala.util.Using(
      scala.io.Source.fromFile(file)
    ) { src =>
      val it = src.getLines().map(_.trim)
      process(it.next())
    }.fold(_ => ???, identity)
  }

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }
}
