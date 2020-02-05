package aa_fp

/**
  *
  * 1. better inference
  * 2. implicit variables
  * 3. referring to vars in different scopes
  * 4. own control structures
  *    https://alvinalexander.com/scala/using-control-structure-beginning-scala-david-pollak
  *
  */
object Fps008MultipleParams extends App {

  def whlist(cond: => Boolean)(body: => Unit): Unit =
    while (cond) body

  var i = 0
  whlist (i<5) {
    println(i)
    i+=1
  }

  def whileNotBoth(cond1: => Boolean)(cond2: => Boolean)(body: => Unit): Unit =
    while (!(cond1 && cond2)) body

  var j = 1
  whileNotBoth(j % 3 == 0)(j % 2 == 0) {
    println(s"J:$j")
    j += 1
  }

  def weirdAdd(a: Int = 1)(b: Int = a): Int = a + b
  println(weirdAdd(10)(2)) // 12
  println(weirdAdd(10)())  // 20
  println(weirdAdd()())    // 2

}
