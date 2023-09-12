package lee

object Lee extends App {

  sealed trait Cell
  case object Empty extends Cell
  case object Obstacle extends Cell
  case class Path(n: Int) extends Cell

  val data: Array[Array[Cell]] = Array.ofDim[Cell](20, 10)

  def isOccupied(x: Int, y: Int): Boolean =
    data(y)(x) == Obstacle

  def doSomething(x: Int, y: Int): Unit = {

    val formatetd: String = data(y)(x) match {
      case Empty    => "EMPTY"
      case Obstacle => "OBSTACLE"
      case Path(n)  => s"PATH:$n"
    }

    println(formatetd)

  }

  data(3)(3) = Empty
  data(4)(4) = Obstacle
  data(5)(5) = Path(34)

  doSomething(3,3)
  doSomething(4,4)
  doSomething(5,5)





}
