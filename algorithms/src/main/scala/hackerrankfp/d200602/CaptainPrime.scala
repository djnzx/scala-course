package hackerrankfp.d200602

object CaptainPrime {
  def pow(n: Int, p: Int): Int = (1 to p).foldLeft(1) { (a, _) => a * n }

  def isPrime(n: Int): Boolean = n match {
    case 1 => false
    case 2 => true
    case _ => (2 to math.sqrt(n).toInt).forall { n % _ != 0 }
  }

  def isPrime(s: String): Boolean = isPrime(s.toInt) 
  
  def isPrime(ns: Seq[Int]): Boolean = ns.forall(isPrime)
  
  def toLeft(n: Int, acc: List[Int]): List[Int] = {
    val next = n/10
    if (next==0) acc
    else toLeft(next, next::acc)
  }

  def toRight(n: Int, p: Int, acc: List[Int]): List[Int] =
    if (p==0) acc
    else {
      val next = n % pow(10, p)
      if (next==0) acc
      else toRight(next, p-1, next::acc)
    }

  def allToLeft(s: String): List[Int] = toLeft(s.toInt, Nil)
  def allToRight(s: String): List[Int] = toRight(s.toInt, s.length-1, Nil)
  
  def process(data: List[String]) = data
    .map { id => (id.contains('0'), isPrime(allToLeft(id)), isPrime(id), isPrime(allToRight(id))) }
    .map {
      case (true, _,     _,     _    ) |
           (_,    _,     false, _    ) => "DEAD"
      case (_,    true,  _,     true ) => "CENTRAL"
      case (_,    true,  _,     false) => "RIGHT"
      case (_,    false, _,     true ) => "LEFT"
      case _                           => "DEAD"
    }

  def body(line: => String): Unit = {
    val N = line.toInt
    val list = (1 to N).map { _ => line }.toList
    process(list).foreach(println)
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("captainprime.txt", body)
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
