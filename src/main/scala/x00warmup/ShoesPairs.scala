package x00warmup

object ShoesPairs extends App {

  def pairs(origin: String) =
    origin.toSeq.foldLeft(0 -> 0)((t, el) => {
    val pair = t._1 + (if (el=='R') 1 else -1)
    pair -> (t._2 + (if (pair==0) 1 else 0))
  })._2

  Map(
    "RL" -> 1,
    "RRLRLRLRLRLL" -> 1,
    "LLLRRR" -> 1,
    "RLRL" -> 2,
    "RLLRLLLRRRRL" -> 4,
  ).foreach(t => assert(t._2 == pairs(t._1)))
}
