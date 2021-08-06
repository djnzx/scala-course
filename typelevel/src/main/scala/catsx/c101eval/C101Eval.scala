package catsx.c101eval

import cats.Eval

/**
  * abstraction about different model of computation
  * - Eval.now    == plain val x, call-by-value, eager
  * - Eval.always == like a function, x :=> A, call-by-name, lazy
  * - Eval.later  == lazy val, calc on the first call and remember
  */
object C101Eval extends App {

  object CallByValue {
    val x = {
      println("Computing X")
      math.random
    }
    println(x)
    println(x)
  }
//  CallByValue

  object CallByName {
    def x = {
      println("Computing X")
      math.random
    }
    println(x)
    println(x)
  }
//  CallByName

  object Lazy {
    lazy val x = {
      println("Computing X")
      math.random
    }
    println(x)
    println(x)
  }
//  Lazy

  /** eager + memo     === val */
  val now: Eval[Double] = Eval.now {
    math.random + 100
  }

  /** lazy + non memo, === def */
  val always: Eval[Double] = Eval.always {
    math.random + 300
  }

  /** lazy + memo      === lazy val */
  val later: Eval[Double] = Eval.later {
    math.random + 200
  }

  /** extracting value */
  val d1: Double = now.value
  val d2: Double = later.value
  val d3: Double = always.value

  object NowExample {
    1 to 3 map { _ => now } map { _.value } foreach println
  }
//  NowExample

  object LaterExample {
    1 to 3 map { _ => later } map { _.value } foreach println
  }
//  LaterExample

  object AlwaysExample {
    1 to 3 map { _ => always } map { _.value } foreach println
  }
//  AlwaysExample

  object CopyOnWrite {
    val maybe2: Eval[Int] = Eval.later {
      println("one"); 1
    }.map { x =>
      println("plus one"); x + 1
    }
  }
  val x = CopyOnWrite.maybe2.value
  println(x)

  /** mapping is always lazy */
  val saying = Eval
    .always { println("Step 1"); "The cat" }
    .map { s => println("Step 2"); s"$s sat on" }
    .memoize // memoize (cache everything until now)
    .map { s => println("Step 3"); s"$s the mat" }

}
