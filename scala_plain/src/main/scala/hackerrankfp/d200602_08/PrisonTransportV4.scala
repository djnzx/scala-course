package hackerrankfp.d200602_08

/**
  * https://www.hackerrank.com/challenges/prison-transport/problem
  * doesn't pass one test
  */
object PrisonTransportV4 {
  // pricing
  def priceOneGroup(n: Int): Int = math.ceil(math.sqrt(n)).toInt
  def priceAllGroups(xs: Iterable[Int]): Int = xs.toVector.map { priceOneGroup }.sum
  def priceTotal(N: Int, xs: Iterable[Int]): Int = N - xs.sum + priceAllGroups(xs)
  
  // processing
  import scala.collection.mutable
  type MMap[A,B] = mutable.Map[A,B]
  type MSet[A] = mutable.Set[A]

  val m: MMap[Int, Int] = mutable.Map.empty
  val mapset: MMap[Int, MSet[Int]]= mutable.Map.empty
  
  def processPair(a: Int, b: Int): Unit = {
    (m.contains(a), m.contains(b)) match {
      case (false, false) =>
        m.addAll(Seq(a -> a, b -> a))       // set_id = a
        mapset.addOne(a -> mutable.Set(a, b)) // set_id -> set_contents

      case (true,  false) =>
        val set_id = m(a)
        m.addOne(b -> set_id)
        mapset(set_id).addOne(b)

      case (false, true) =>
        val set_id = m(b)
        m.addOne(a -> set_id)
        mapset(set_id).addOne(a)
        
      case (true,  true ) => 
        val set_id1 = m(a)
        val set_id2 = m(b)
        val set1 = mapset(set_id1)
        val set2 = mapset(set_id2)
        set1.addAll(set2)
        set2.foreach { k => m.put(k, set_id1) }
    }
  }
  
  def simplify(m: MMap[Int, MSet[Int]]): Iterable[Int] = {
    val a: Iterable[MSet[Int]] = m.values
    val b: Set[MSet[Int]] = a.toSet
    val c: Set[Int] = b.map { _.size }
    c
  }

  def process(N: Int, pairs: Seq[(Int, Int)]): Int = {
    pairs.foreach { case (a, b) => processPair(a, b) }
    println(m)
//    val groups = simplify(m)
//    println(groups)
//    println(s"N = ${N}")
//    priceTotal(N, groups)
    -1
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
  def main(p: Array[String]): Unit = processFile("prison2.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
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
