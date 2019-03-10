package x006

object NextValueApp extends App {
  var counter = 0

  def next = {
    counter += 1
    counter
  }

  val x1 = next
  val x2 = next
  val x3 = next
  val x4 = next
  val x5, x6, x7 = next

  println(x1, x2, x3, x4, x5, x6, x7)
}
