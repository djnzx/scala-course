package fp_red.exercises.c05laziness

object LazyApp extends App {
  // strict
  List(1,2,3,4)     .map(_ + 10).filter(_ %2 == 0).map(_ * 10)
  // lazy
  val r = List(1,2,3,4).view
    .map(x =>  { println(s"$x:map+10");x + 10    })
    .filter(x=> { println(s"$x:filter") ;x %2 == 0 })
    .map(x =>  { println(s"$x:map*10");x * 10    })
    .toList
  println(r)

  def if2[A](cond: Boolean, a: => A, b: => A): A = if (cond) a else b


}
