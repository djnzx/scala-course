package __udemy.scala_beginners.lectures.part3fp._tuples

object TupleAndPatternMathingApp extends App {
  // tuples = finite ordered "lists"
  val aTuple = (2, "hello, Scala")  // Tuple2[Int, String] = (Int, String)
  println(aTuple._1)  // 2
  println(aTuple.copy(_2 = "goodbye Java"))
  println(aTuple.swap)  // ("hello, Scala", 2)

  val aTuple42 : Tuple4[Int, String, Float, Boolean] = (1, "Alex", 500.0f, true)
  val aTuple43 : (Int, String, Float, Boolean) = (1, "Alex", 500.0f, true)
  val aTuple41 = (1, "Alex", 500.0f, true) // inferred (Int, String, Float, Boolean)
  println(aTuple41)
  println(aTuple41._1)
  println(aTuple41._2)
  println(aTuple41._3)
  println(aTuple41._4)
  println(aTuple41.toString)
  println(aTuple41.copy(_2 = "Dima"))

  val glazedDonutTuple = Tuple2("Glazed Donut", "Very Tasty")

  val glazedDonut = Tuple3("Glazed Donut", "Very Tasty", 2.50)
  val strawberryDonut = Tuple3("Strawberry Donut", "Very Tasty", 2.50)
  val plainDonut = Tuple3("Plain Donut", "Tasty", 2)
  val donutList = List(glazedDonut, strawberryDonut, plainDonut, ("Alex", "Dima", 9.99), ("King", "don't care", 10000.0))

  val priceOfPlainDonut: Unit = donutList.foreach { tuple => {
    tuple match {
      case ("Plain Donut", _, price) => println(s"Donut type = Plain Donut, price = $price")
      case d if d._1 == "Glazed Donut" => println(s"Donut type = ${d._1}, price = ${d._3}")
      case _ => None
    }
  }}
  priceOfPlainDonut

  println("-------")
  donutList.foreach {
    case ("Plain Donut", taste, price) => println(s"R1: Donut type = Plain Donut, price = $price")
    case d if d._1 == "Glazed Donut" => println(s"R2: Donut type = ${d._1}, price = ${d._3}")
    case t if t._1.startsWith("Alex") || t._2.contains("never mind") => println("R3: "+t.toString())
    case (_, "don't care",price) => println(s"R4: $price")
    case (a,b,c) => println(s"R5: $a $b $c")
    case _ => println("R99: Anything else")
  }
  println("-------")

  val priceOfDonut: Any = 2.50f // Double by default
  val priceType = priceOfDonut match {
    case _: Int => "Int"
    case _: Double => "Double"
    case _: Float => "Float"
    case _: String => "String"
    case _: Boolean => "Boolean"
    case _: Char => "Char"
    case price: Long => "Long"
  }
  println(s"Donut price type = $priceType")


}
