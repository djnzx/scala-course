package aa_fp

object Fps013ForComprehensions extends App {
  /**
    * no more for-loops.
    * now and then:
    * - for-comprehensions
    * - for-expressions
    *
    * for can contain:
    * - generator
    * - filter
    * - definition
    *
    * every for-comprehension begins with a generator
    * for-comprehension can have many generators
    *
    * map/flatMap
    * withFilter
    * foreach
    */

  case class Figure(sides: Int, area: Int)
  val figures = Seq(Figure(3,9), Figure(4,16), Figure(5,25))

  val figures2: Seq[Int] = for {
    figure <- figures // generator, actually map/flatMap
    a = figure.area  // definition
    if a > 10       // filter
  } yield figure.sides

  class Sequence[A] private (from: A*) {
    // varargs
    private val storage = scala.collection.mutable.ArrayBuffer[A]()
    storage ++= from

    // to support: `for (s <- s1) println(s)`. naive implementation
    def foreach(code: A => Unit): Unit = storage.foreach(code)

    // to support: `for / yield`. naive implementation
    def map[B](f: A => B): Sequence[B] = {
      // solution 1.
//      val x: mutable.Seq[B] = for { el <- storage } yield f(el)
//      Sequence(x)
      // solution 2.
      val mapped = storage.map(f)
      Sequence(mapped.toSeq: _*)
    }

    // to support: `for / yield`. for nested comprehensions
    // we can treat flatMap as map + flatten
    def flatMap[B](f: A => Sequence[B]): Sequence[B] = flattenLike(map(f))

    private def flattenLike[B](ssb: Sequence[Sequence[B]]): Sequence[B] = {
      val buffer = scala.collection.mutable.ArrayBuffer[B]()
      for {
        sb <- ssb
        b <- sb
      } buffer += b
      Sequence(buffer.toSeq: _*)
    }

    // to support `if inside for`
    // but actually it should be implemented in LAZY mode
    def withFilter(p: A => Boolean): Sequence[A] = Sequence(storage.filter(p).toSeq: _*)

    // only for printing purposes
    override def toString: String = storage.mkString("Sequence[",",","]")
  }

  object Sequence {
    // it takes vararg, immediately convert it to Iterable and splat it again
    def apply[A](xs: A*): Sequence[A] = new Sequence(xs :_*)
  }

  val s1 = Sequence(1,2,3,4,5,6,7)
  val s2 = Sequence("a","b","c")
  val s3 = Sequence(Figure(3,9), Figure(4,16), Figure(5,25))

  /**
    * in order to make it working, we need to implement
    * `def foreach(code: A => Unit): Unit`
    */
  for (s <- s1) println(s)

  /**
    * in order to make it working, we need to implement
    * `def map[B](f: A => B): Sequence[B]`
    */
  val s1mapped: Sequence[String] = for {
    v <- s1
    v2 = v * 10
    s = v2.toString
  } yield s

  /**
    * in order to make it working, we need to implement
    * `def withFilter(p: A => Boolean): Sequence[A]`
    */
  val s1filtered: Sequence[Int] = for {
    v <- s1
    if v > 2
  } yield v

  case class Person(name: String)
  val men = Sequence(Person("Alex"), Person("Dima"), Person("Sergey"))
  val alexFriends = Sequence(Person("Jim"), Person("John"), Person("Joe"))

  /**
    * in order to make it working, we need to implement
    * `def map[B](f: A => B): Sequence[B]`
    */
  val friends: Sequence[(String, String)] = for {
    man <- men          // flatMao
    af  <- alexFriends  // map
    if man.name == "Alex"
  } yield (man.name, af.name)
  println(friends)

}
