package __udemy.scala_beginners.lectures.part2oop._done

object Mappers extends App {

  object UTIL {
    def incrementer(s: String): String = s.map(c => (c + 1).toChar)
    def incrementer(c: Char) = (c + 1).toChar
  }

  def incrementer(c: Char) = (c + 1).toChar

  println(UTIL.incrementer("HAL"))
  println("HAL".map(incrementer))
  println("HAL".map(UTIL.incrementer))


  println("13".toInt)


  val fruits = Seq("apple", "banana", "orange")
  val FRUITS = fruits.map(_.toUpperCase)
  println(fruits)
  println(FRUITS)
  val x1 = fruits.flatMap(_.toUpperCase)
  val x2 = fruits.flatten
  println(x2)

  def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  }

  val strings = Seq("1", "2", "foo", "3", "bar")
  val ints = strings.map(toInt)
  val ints2 = strings.flatMap(toInt)
  println(ints)
  println(ints2)

  val chars = 'a' to 'c'
  val perms = chars flatMap { a =>
    chars flatMap { b =>
      if (a != b) Seq("%c%c".format(a, b))
      else Seq()
    }
  }

  println(chars)
  println(perms)
  /*
    1.  Generic trait MyPredicate[-T] with a little method test(T) => Boolean
    2.  Generic trait MyTransformer[-A, B] with a method transform(A) => B
    3.  MyList:
        - map(transformer) => MyList
        - filter(predicate) => MyList
        - flatMap(transformer from A to MyList[B]) => MyList[B]

        class EvenPredicate extends MyPredicate[Int]
        class StringToIntTransformer extends MyTransformer[String, Int]

        [1,2,3].map(n * 2) = [2,4,6]
        [1,2,3,4].filter(n % 2) = [2,4]
        [1,2,3].flatMap(n => [n, n+1]) => [1,2,2,3,3,4]
   */

}
