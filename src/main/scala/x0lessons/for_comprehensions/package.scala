package x0lessons

import scala.collection.mutable.ArrayBuffer

package object for_comprehensions {
  case class Person(name: String)

  case class Sequence[A](initial: A*) { // varargs
    // create new storage
    private val storage = ArrayBuffer[A]() // Java ArrayList
    // copy all new elements came from constructor
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

}
