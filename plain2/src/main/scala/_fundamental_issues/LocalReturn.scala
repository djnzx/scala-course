package _fundamental_issues

object LocalReturn extends App {

  def doSomething(): Unit = List(1, 2, 3)
    .foreach { x =>
      println(x)
      return // here it quits from the doSomething scope
    }

  doSomething()

}
