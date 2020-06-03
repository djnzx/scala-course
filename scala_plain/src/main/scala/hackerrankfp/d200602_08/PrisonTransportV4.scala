package hackerrankfp.d200602_08

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * https://www.hackerrank.com/challenges/prison-transport/problem
  * doesn't pass one test
  */
object PrisonTransportV4 {
  type MMap[A,B] = mutable.Map[A,B]
  type MSet[A] = mutable.Set[A]
  case class Hold(ids: ArrayBuffer[Int], cnt: ArrayBuffer[Int], 
                  set: MMap[Int, MSet[Int]] = mutable.Map.empty, 
                  join: ArrayBuffer[(Int, Int)] = ArrayBuffer.empty) {
    def print = {
      println(s"ids: $ids")
      println(s"cnt: $cnt")
      println(s"set: $set")
    }
  }
  // pricing
  def priceOneGroup(n: Int): Int = math.ceil(math.sqrt(n)).toInt
  def priceAllGroups(xs: Iterable[Int]): Int = xs.toVector.map { priceOneGroup }.sum
  def priceTotal(N: Int, xs: Iterable[Int]): Int = N - xs.sum + priceAllGroups(xs)
  var i = 0
  def processPair(a: Int, b: Int, h: Hold): Unit = {
//    println(i)
//    i += 1
    (h.ids(a), h.ids(b)) match {
      case (0, 0) =>
//        println(s"A: $a $b")
        // point to group
        val gid = a
        h.ids(a) = gid
        h.ids(b) = gid
        // count the group elements
        h.cnt(gid) = 2
        // put the keys
        h.set.addOne(gid -> mutable.Set(a, b))

      case (gida, 0) =>
//        println(s"B: $a $b")
        // point to group
        h.ids(b) = gida
        // increment count
        h.cnt(gida) += 1
        // add the element
        h.set(gida).add(b)

      case (0, gidb) =>
//        println(s"C: $a $b")
        // point to group
        h.ids(a) = gidb
        // increment count
        h.cnt(gidb) += 1
        // add the element
        h.set(gidb).add(a)
        
      case (gida, gidb) =>
        println(s"D: $a $b")
        // set a += set b
        println("D0")
        println(s"cnta: ${h.cnt(a)}, cntb: ${h.cnt(b)}")
        println(s"gida: $gida, gidb: $gidb")
        val seta: Option[MSet[Int]] = h.set.get(a)
        val setb: Option[MSet[Int]] = h.set.get(b)
        println(seta)
        println(setb)
        h.set(gida).addAll(h.set(gidb))
        println("D1")
        // add counter
        h.cnt(gida) += h.cnt(gidb)
        println("D2")
        // clear 2nd counter
        h.cnt(gidb) = 0
        println("D3")
        // rewrite group number
        h.set(gidb).foreach(k => h.ids(k) = gida)
        println("D4")
        // remove group b
        h.set.remove(gidb)
        println("D5")
    }
  }
  
  def simplify(cnt: ArrayBuffer[Int]): Iterable[Int] = cnt.toVector.filter(_ > 0)
  
  def process(N: Int, pairs: Seq[(Int, Int)]): Int = {
    val h = Hold(ArrayBuffer.fill[Int](N+1)(0), ArrayBuffer.fill[Int](N+1)(0))
    pairs.foreach { case (a, b) => processPair(a, b, h) }
    val groups = simplify(h.cnt)
    h.print
    println(s"g:$groups")
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
