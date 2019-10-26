package x0lessons.for_comprehensions
import scala.collection.mutable.ArrayBuffer

// https://alvinalexander.com/scala/fp-book/how-to-make-class-work-generator-in-for-expression
object My4FlatMapAbleApp extends App {

  // to use '<-' in for-comprehensions,
  // and produce, collect the data
  // you must define only 'map[B]' which consumes a function 'A => B'
  // you must define only 'flatMap[B]' which consumes a function 'A => Class[B]'
  // MULTI-level !
  case class Person(name: String)

  case class Sequence[A](initial: A*) { // varargs
    private val storage = ArrayBuffer[A]() // Java ArrayList
    storage ++= initial

    def foreach(f: A => Unit): Unit = storage.foreach(f)
    def map[B](f: A => B): Sequence[B] = Sequence(storage.map(f).toSeq: _*)
    def flatMap[B](f: A => Sequence[B]): Sequence[B] = flattenLike(map(f))
    private def flattenLike[B](nested: Sequence[Sequence[B]]): Sequence[B] ={
      var xs = ArrayBuffer[B]()
      for (list: Sequence[B] <- nested) {
        for (item <- list) {
          xs += item
        }
      }
      Sequence(xs.toSeq: _*)
    }
    def withFilter(p: A => Boolean): Sequence[A] = Sequence(storage.filter(p).toSeq: _*) // must be non-strict
  }

  // my general intention
  val strings = Sequence("Java", "Scala", "Haskell")
  val numbers = Sequence(75, 42, 33)
  val persons = Sequence(Person("Alex"), Person("Dima"))
  val persons2 = Sequence(Person("Masha"), Person("Marina"))
  // and have them iterable

  // to run this code, we need to implement 'foreach'
  for {
    item <- strings
  } println(item)

  // to run this code, we need to implement 'map'
  val outcome1 = for {
    item <- strings
  } yield item
  println(outcome1)

  // to run this code, we need to implement 'withFilter'
  val outcome2 = for {
    item <- strings
    if item.startsWith("S")
  } yield item
  println(outcome2)

  // to run this code, we need to implement 'flatMap'
  val outcome3: Sequence[(Person, Person)] = for {
    item1 <- persons  // flatMap req
    item2 <- persons2 // map req
    if item1 != item2
  } yield item1 -> item2 // this is always a List because of a lot of results
  println(outcome3)

}
