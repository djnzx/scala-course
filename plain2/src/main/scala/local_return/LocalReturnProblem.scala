package local_return

object LocalReturnProblem extends App {

  def something: Unit = List(1, 2, 3)
    .foreach { x =>
      println(x)
      return
    }

  something

}
