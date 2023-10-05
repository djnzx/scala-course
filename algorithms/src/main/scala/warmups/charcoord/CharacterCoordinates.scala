package warmups.charcoord

object CharacterCoordinates extends App {
  val origin = "Hello, my dear friend, please keep learning, I'll guarantee you'll reach the moment you understand everything"

  val outcome = origin
    .zip(LazyList.from(1))
    .filter { case (ch, _) => Character.isAlphabetic(ch) }
    .map { case (c, pos) => c.toLower -> pos }
    .groupBy { case (c, _) => c }
    .map { case (c, ps) => c -> ps.map { case (_, pos) => pos } }
    .toVector
    .sortBy { case (_, ps) => ps.length }(Ordering.Int.reverse)
    .map { case (c, pos) => s"$c:${pos.length}:<${pos.mkString(",")}>" }
    .mkString("\n")

  println(outcome)
}
