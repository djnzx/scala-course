package x003

object Iterations extends App {
  val cars = List("audi", "bmw", "chrysler", "lincoln")
  // List[String]
  val carsA = Array("audi", "bmw", "chrysler", "lincoln")
  // Array[String]
  println(cars)
  val carsC = for (c <-cars) yield c.capitalize
  println(carsC)
  // List[String]
  for (car <- carsC) println(car)
  for (car <- carsC) {
    val s = car.toUpperCase
    println(s)
  }
  val capitalized = for (car <- carsC) yield car.toUpperCase
  // https://stackoverflow.com/questions/2712877/difference-between-array-and-list-in-scala
  // List[String]
  println(capitalized)

  for ( i <- 0 until carsC.length) {
    println(s"$i is ${carsC(i)}")
  }

  for ( i <- carsC.indices) {
    println(s"$i is ${carsC(i)}")
  }

  for ((item, index) <- carsC.zipWithIndex) {
    println(s"$index is $item")
  }

  for (i <- 1 to 3) println(i)
  // Range.Inclusive

  println
  for (i <- 1 to 10 if i < 4) println(i)

  val names = Map("name" -> "Alex", "age" -> 42)
  // scala.collection.immutable.Map[String,Any]
  for ((k, v) <- names) println(s"key: $k, v: $v")

  carsC.foreach(el => el.toUpperCase)
  println(carsC)
  val uppercased = carsC.map(el => el.toUpperCase)
  println(uppercased)
}
