package cookbook.x010

object C10_16 extends App {
  val bag = List("1","2","3","four","five")

  def toInt(x: String) = {
    try {
      Some(Integer.parseInt(x))
    } catch {
      case e :Exception => None
    }
  }

  println(bag.map(toInt))
  println(bag.flatMap(toInt).sum)


  val r0 = List.range(1,50)
  val r = List.range(1,50).takeRight(5)
  val r2 = List.range(1,50).takeWhile(_ < 10)
  println(r)
  println(r2)
  println(r0.partition(_ > 20))
//  println(r0.par) // parallel - deprecated in 2.13
}
