package udemy.scala_beginners.lectures.part3fp._zip

object ZipWithIndexUnzipApp extends App {
  val days = List("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

  val t1= days
      .zipWithIndex
      .map(el => (el._2+1, el._1)) // initially numbering begins from zero, I'd like number days from 1
  println(t1) // List((0,Monday), (1,Tuesday), (2,Wednesday), (3,Thursday), (4,Friday), (5,Saturday), (6,Sunday))

  val (a1, a2) = t1.unzip
  println(a1) // List(0, 1, 2, 3, 4, 5, 6)
  println(a2) // List(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday)

  val daysWithIndexes1 = days zip (Stream from 1)
  val daysWithIndexes2 = daysWithIndexes1.map(_.swap)
  println(daysWithIndexes1)
  println(daysWithIndexes2)
}
