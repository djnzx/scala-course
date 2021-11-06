package cookbook.x003

object P03_16MatchExceptions extends App {
  try {
    1 / 0
  } catch {
    case e: ArithmeticException => println(s"exception: ${e.getClass.getSimpleName}, message: ${e.getMessage}")
    case e: Throwable => println(s"exception: ${e.getClass.getSimpleName}, message: ${e.getMessage}")
    case _: Throwable => println("smth other went wrong ;(")
  }
}
