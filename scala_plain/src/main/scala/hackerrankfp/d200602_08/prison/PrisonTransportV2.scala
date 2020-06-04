package hackerrankfp.d200602_08.prison

import scala.collection.mutable

/**
  * https://www.hackerrank.com/challenges/prison-transport/problem
  * doesn't pass one test
  */
object PrisonTransportV2 {

  type MMap[A,B] = mutable.Map[A,B]
  type MSet[A] = mutable.Set[A]
  
  import scala.collection.mutable

  def priceOneGroup(n: Int): Int = math.ceil(math.sqrt(n)).toInt
  
  def priceAllGroups(xs: Iterable[Int]): Int = xs.toVector.map { priceOneGroup }.sum
  
  def priceTotal(N: Int, xs: Iterable[Int]): Int = N - xs.sum + priceAllGroups(xs)
  
  def processPair(a: Int, b: Int, m: MMap[Int, MSet[Int]]): MMap[Int, MSet[Int]] = {
    (m.contains(a), m.contains(b)) match {
      case (false, false) =>
        val ms = mutable.Set(a, b)
        m.addOne(a -> ms)
        m.addOne(b -> ms)

      case (true,  false) =>
        val ms = m(a) 
        ms.add(b)  
        m.addOne(b -> ms)

      case (false, true ) =>
        val ms = m(b)
        ms.add(a)
        m.addOne(a -> ms)
        
      case (true,  true ) => 
        val ms1 = m(a)
        val ms2 = m(b)
        ms2.foreach(m.put(_, ms1))
        ms1.addAll(ms2)
    }
    m
  }
  
  def simplify(m: MMap[Int, MSet[Int]]): Iterable[Int] = {
    val a: Iterable[MSet[Int]] = m.values
    val b: Set[MSet[Int]] = a.toSet
    val c: Set[Int] = b.map { _.size }
    c
  }

  def process(N: Int, pairs: Seq[(Int, Int)]): Int = {
    val map = pairs.foldLeft(mutable.Map.empty[Int, MSet[Int]]) { case (m, (a,b)) => processPair(a, b, m) }
    println(map)
    val groups = simplify(map)
    println(groups)
    println(s"N = ${N}")
    priceTotal(N, groups)
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
