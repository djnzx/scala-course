package http4middle

object MiddlewareBasicsApp extends App {

  // Int => Int
  // Request => Response
  def inc(i: Int): Int = i + 1

  def withAny(f: Int => Int, g: Any => Unit): Int => Int = {
    g()
    f
  }

  def withLog(f: Int => Int) =
    withAny(f, _ => println("Logged1"))

  val incLogged1: Int => Int = withLog(inc)
  val incLogged2: Int => Int = withAny(inc, _ => println("Logged2"))

  val six1: Int = incLogged1(5)
  val six2: Int = incLogged2(5)

}
