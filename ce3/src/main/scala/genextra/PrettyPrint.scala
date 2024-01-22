package genextra

trait PrettyPrint {

  def printLine() = println("=" * 50)

  def pprintln[A](x: A): Unit = pprint.pprintln(x)

}
