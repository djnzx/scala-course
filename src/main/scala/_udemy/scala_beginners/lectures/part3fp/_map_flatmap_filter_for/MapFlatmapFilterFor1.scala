package _udemy.scala_beginners.lectures.part3fp._map_flatmap_filter_for

object MapFlatmapFilterFor1 extends App {
  // print all combinations among three lists
  val numbers = List(1,2,3,4)
  val chars = List('a','b','c','d')
  val colors = List("black", "white")

  // List("a1", "a2"... "d4")

  // "iterating"
  val combinations = numbers
    //.filter(_ % 2 == 0)
    .flatMap(n => chars
    .flatMap(c => colors
      .map(color => "" + c + n + "-" + color)
    )
  )
  println(combinations)


  // foreach
  val list = List(1,2,3)
  list.foreach(println)

  // for-comprehensions
  val forCombinations = for {
    n <- numbers if n % 2 == 0
    c <- chars
    color <- colors
  } yield "" + c + n + "-" + color
  println(forCombinations)

  for {
    n <- numbers
  } println(n)

  // syntax overload
  list.map { x =>
    x * 2
  }

  /*
    1.  MyList supports for comprehensions?
        map(f: A => B) => MyList[B]
        filter(p: A => Boolean) => MyList[A]
        flatMap(f: A => MyList[B]) => MyList[B]
    2.  A small collection of at most ONE element - Maybe[T]
        - map, flatMap, filter
   */


}
