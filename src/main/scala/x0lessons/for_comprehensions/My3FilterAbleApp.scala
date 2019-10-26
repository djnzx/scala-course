package x0lessons.for_comprehensions

// https://alvinalexander.com/scala/fp-book/how-to-make-class-work-generator-in-for-expression
object My3FilterAbleApp extends App {

  // to use 'if' in for-comprehensions
  // and filtering your data
  // you must define only 'withFilter' method
  class MyMapAble {
    private val data: List[String] = List("Java", "Scala", "Haskell")
    def map[B](f: String => B): Seq[B] = data.map(f)
    def withFilter(p: String => Boolean): Seq[String] = data.filter(p)
  }

  val instance = new MyMapAble
  val outcome = for {
    s <- instance
    if s.startsWith("S")
  } yield s
  println(outcome)

}
