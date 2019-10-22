package x90

object CharacterCoordinates extends App {
  val origin = "Hello, my dear friend, please keep learning, I'll guarantee you'll reach the moment you understand everything"
  val outcome = origin.zip(1 to origin.length)
        .filter(t => Character.isAlphabetic(t._1))
        .map(t => t._1.toLower -> t._2)
        .groupBy(_._1)
        .map((el: (Char, IndexedSeq[(Char, Int)])) => el._1 -> el._2.length -> el._2.map(x => x._2))
        .toList
        .sortBy(el => el._1)
        .map(el => s"${el._1._1}:${el._1._2}:<${el._2.map(_.toString).reduce((s1, s2) => s"$s1,$s2")}>")
        .reduce((s1, s2) => s"$s1,$s2")
        .formatted(s"[%s]")
  println(outcome)
}
