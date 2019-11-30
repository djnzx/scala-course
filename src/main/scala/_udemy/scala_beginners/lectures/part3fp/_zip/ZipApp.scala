package _udemy.scala_beginners.lectures.part3fp._zip

object ZipApp extends App {
  val donuts: Seq[String] = Seq("Plain Donut", "Strawberry Donut", "Glazed Donut")
  val prices: Seq[Double] = Seq(1.5, 2.0, 2.5)
  val donutWPrices1 = donuts.zip(prices) // List((Plain Donut,1.5), (Strawberry Donut,2.0), (Glazed Donut,2.5))
  val donutWPrices2 = donuts zip prices // List((Plain Donut,1.5), (Strawberry Donut,2.0), (Glazed Donut,2.5))

  println(donuts)
  println(prices)
  println(donutWPrices1)
  println(donutWPrices2)
}
