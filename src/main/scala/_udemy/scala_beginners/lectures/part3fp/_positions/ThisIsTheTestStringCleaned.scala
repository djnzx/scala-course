package _udemy.scala_beginners.lectures.part3fp._positions

object ThisIsTheTestStringCleaned extends App {
  def m1(input: String): String =
  input
    .zip(Stream from 1)
    .filter(_._1.isLetter)
    .groupBy(_._1)
    .mapValues(_.map(_._2))
    .mapValues(_.mkString("<",".",">"))
    .toList
    .sortBy(_._1)
    .map(t => s"${t._1}:${t._2}")
    .mkString(", ")
    //.foreach(print)

  m1("This is the test string").foreach(print)
}
