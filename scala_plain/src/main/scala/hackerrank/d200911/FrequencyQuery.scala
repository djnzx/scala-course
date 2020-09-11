package hackerrank.d200911

/**
  * https://www.hackerrank.com/challenges/frequency-queries/problem
  * one test case Terminated due to timeout :(
  * 36.67 / 40
  */
class FrequencyQuery {

  def freqQuery(queries: Array[Array[Int]]): Array[Int] = {
    sealed trait Cmd
    case class Inc(x: Int) extends Cmd
    case class Dec(x: Int) extends Cmd
    case class Query(n: Int) extends Cmd

    def inc(map: Map[Int, Int], key: Int) = map.updatedWith(key) {
      case None    => Some(1)
      case Some(v) => Some(v + 1)
    }

    def dec(map: Map[Int, Int], key: Int) = map.updatedWith(key) {
      case None   => None
      case Some(1) => None
      case Some(v) => Some(v - 1)
    }

    def query(map: Map[Int, Int], cnt: Int) = map.count { case (_, c) => c == cnt } match {
      case 0 => 0
      case _ => 1
    }

    queries
      .map({
        case Array(1, x) => Inc(x)
        case Array(2, x) => Dec(x)
        case Array(3, n) => Query(n)
      })
      .foldLeft((Map.empty[Int, Int], List.empty[Int])) {
        case ((map, out), cmd) => cmd match {
          case Inc(x) => (inc(map, x), out)
          case Dec(x) => (dec(map, x), out)
          case Query(n) => (map, query(map, n) :: out)
        }
      } 
    match {
      case (_, l) => l.reverse.toArray
    }
  }

}
