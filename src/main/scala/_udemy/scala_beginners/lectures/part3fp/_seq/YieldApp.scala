package _udemy.scala_beginners.lectures.part3fp._seq

object YieldApp extends App {
  val ex1 = for (i <- 1 to 5) yield i
  println(ex1)
  // scala.collection.immutable.IndexedSeq[Int] = Vector(1, 2, 3, 4, 5)

  val ex2 = for (i <- 1 to 5) yield i * 2
  println(ex2)
  // scala.collection.immutable.IndexedSeq[Int] = Vector(2, 4, 6, 8, 10)

  val ex3 = for (i <- 1 to 5) yield i % 2
  println(ex3)
  // scala.collection.immutable.IndexedSeq[Int] = Vector(1, 0, 1, 0, 1)

  val a1 = Array(1, 2, 3, 4, 5) // Array[Int]
  println(a1)

  val a2 = for (e <- a1) yield e // Array[Int] = Array(1, 2, 3, 4, 5)
  val a3 = for (e <- a1) yield e * 2
  val a4 = for (e <- a1) yield e % 2

  val a5 = for (e <- a1 if e > 2) yield e // Array[Int] = Array(3, 4, 5)

/*
  def getQueryAsSeq(query: String): Seq[MiniTweet] = {
    val queryResults = getTwitterInstance.search(new Query(query))
    val tweets = queryResults.getTweets  // java.util.List[Status]
    for (status <- tweets) yield ListTweet(status.getUser.toString, status.getText, status.getCreatedAt.toString)
  }
*/


}
