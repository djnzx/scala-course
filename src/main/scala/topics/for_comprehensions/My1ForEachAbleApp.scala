package topics.for_comprehensions

// https://alvinalexander.com/scala/fp-book/how-to-create-sequence-class-in-for-expression
object My1ForEachAbleApp extends App {

  // to use '<-' in for-comprehensions
  // and use action at the end (println, for example)
  // you must define only 'foreach' which consumes 'String' and returns 'Unit'
  class MyForEachAble {
    private val data: List[String] = List("Java", "Scala", "Haskell")
    def foreach(f: String => Unit): Unit = data.foreach(f)
  }

  val instance = new MyForEachAble
  for {
    i <- instance
  } println(i)

}
