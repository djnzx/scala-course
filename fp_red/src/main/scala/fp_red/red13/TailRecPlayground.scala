package fp_red.red13

/**
  * mutual recursion experiments
  */
object TailRecPlayground {
  import IO2b._

  def isOdd(n: Int): TailRec[Boolean] =
    if (n == 0) Return(false)
    else FlatMap(Return(false), (_: Boolean) => isEven(n - 1))

  def isEven(n: Int): TailRec[Boolean] =
    if (n == 0) Return(true)
    else FlatMap(Return(true), (_: Boolean) => isOdd(n - 1))

  def runEven(n: Int) = run(isEven(n))
  def runOdd(n: Int) = run(isOdd(n))
}
