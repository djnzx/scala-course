package misc

object App76 extends App {

  val f = (x: Int) => x + 1
  val g = (x: Int) => x * 2

  val h = f andThen g

  val z = h(6)

}
