package topics.for_comprehensions

// https://alvinalexander.com/scala/fp-book/how-to-make-class-work-generator-in-for-expression
object My2MapAbleApp extends App {

  // to use '<-' in for-comprehensions
  // and produce, collect the data
  // you must define only 'map[B]' which consumes a function 'A => B'
  // but only for one-level !
  class MyMapAble {
    private val data: List[String] = List("Java", "Scala", "Haskell")
    def map[B](f: String => B): List[B] = data.map(f)
  }

  val instance = new MyMapAble
  val outcome = for {
    i <- instance
  } yield i
  println(outcome)

}
