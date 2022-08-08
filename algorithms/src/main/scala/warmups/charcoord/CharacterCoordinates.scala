package warmups.charcoord

object CharacterCoordinates extends App {
  val origin = "Hello, my dear friend, please keep learning, I'll guarantee you'll reach the moment you understand everything"
  val outcome = origin
    .zip(LazyList.from(1))
    .filter { case (ch, _) => Character.isAlphabetic(ch) }
    .map(t => t._1.toLower -> t._2)
    .groupBy(_._1)
    .map { el => el._1 -> el._2.length -> el._2.map(x => x._2) }
    .toVector
    .sortBy(el => el._2.length)((x, y) => y - x)
    .map(el => s"${el._1._1}:${el._1._2}:<${el._2.map(_.toString).reduce((s1, s2) => s"$s1,$s2")}>")
    .reduce((s1, s2) => s"$s1\n$s2")
  println(outcome)
}
