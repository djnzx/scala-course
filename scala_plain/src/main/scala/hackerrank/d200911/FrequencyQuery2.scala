package hackerrank.d200911

/**
  * https://www.hackerrank.com/challenges/frequency-queries/problem
  * 40 / 40
  */
object FrequencyQuery2 {

  sealed trait Cmd extends Product with Serializable
  case class Inc(x: Int) extends Cmd
  case class Dec(x: Int) extends Cmd
  case class Query(n: Int) extends Cmd

  def toCommands(queries: Array[Array[Int]]) =
    queries.map({
      case Array(1, x) => Inc(x)
      case Array(2, x) => Dec(x)
      case Array(3, n) => Query(n)
    })

  val inc_fn: Option[Int] => Option[Int] = {
    case None    => Some(1)
    case Some(v) => Some(v + 1)
  }
  
  val dec_fn:  Option[Int] => Option[Int] = {
    case None | Some(1) => None
    case Some(v)        => Some(v - 1)
  }
  
  def inc(map: Map[Int, Int], key: Int) = map.updatedWith(key)(inc_fn)
  def dec(map: Map[Int, Int], key: Int) = map.updatedWith(key)(dec_fn)

  def incq(qmap: Map[Int, Int], cnt: Int) = qmap
    .updatedWith(cnt)(dec_fn)
    .updatedWith(cnt+1)(inc_fn)

  def decq(qmap: Map[Int, Int], cnt: Int) = qmap
    .updatedWith(cnt)(dec_fn)
    .updatedWith(cnt-1)(inc_fn)

  def query(qmap: Map[Int, Int], cnt: Int) = qmap.contains(cnt) match {
    case true => 1
    case _    => 0
  }

  def fold(cmds: Array[Cmd]) = cmds
    .foldLeft((Map.empty[Int, Int], Map.empty[Int, Int], List.empty[Int])) {
      case ((map, qmap, out), cmd) => cmd match {
        case Inc(x) =>
          val nx = map.getOrElse(x, 0)
          (inc(map, x), incq(qmap, nx), out)
        case Dec(x) =>
          val nx = map.getOrElse(x, 0)
          (dec(map, x), decq(qmap, nx), out)
        case Query(n) =>
          (map, qmap, query(qmap, n) :: out)
      }
    }

  def freqQuery(queries: Array[Array[Int]]): Array[Int] =
    fold(toCommands(queries))
    match {
      case (_, _, l) => l.reverse.toArray
    }

}
