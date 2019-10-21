package x90

object CharacterCoordinated extends App {
  val origin = "Hello, my dear friend, please keep learning, I'll guarantee you'll reach the moment you understand everything"
  val outcome = origin.zip(1 to origin.length)
        .filter(t => Character.isAlphabetic(t._1))
        .map(t => t._1.toLower -> t._2)
        .groupBy(_._1)
        .toSet
        .map((el: (Char, IndexedSeq[(Char, Int)])) => el._1 -> el._2.length -> el._2.map(x => x._2))
        .toList
        .sortBy(el => el._1)
        .map(el => s"${el._1._1}:${el._1._2}:<${el._2.map(_.toString).reduce((s1, s2) => s"$s1,$s2")}>")
        .reduce((s1, s2) => s"$s1,$s2")
  println(outcome)
//  a:7:<13,27,38,53,55,70,96>,c:1:<71>,d:4:<11,21,91,98>,e:16:<2,12,19,26,29,32,33,37,58,59,69,76,81,92,100,102>,f:1:<16>,g:3:<43,51,109>,h:4:<1,72,75,106>,i:4:<18,41,46,107>,k:1:<31>,l:8:<3,4,25,36,48,49,65,66>,m:3:<8,78,80>,n:8:<20,40,42,56,82,90,97,108>,o:4:<5,62,79,86>,p:2:<24,34>,r:7:<14,17,39,54,68,93,103>,s:2:<28,94>,t:5:<57,74,83,95,105>,u:4:<52,63,87,89>,v:1:<101>,y:4:<9,61,85,104>

}
