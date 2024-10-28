package dynamic

import scala.language.dynamics

case class Person(name: String) extends Dynamic {

  /** p.jump */
  def selectDynamic(method: String) =
    println(s"$name is ${method}ing")

  /** p.go(3) */
  def applyDynamic(method: String)(p1: Int) =
    println(s"$name is ${method}ing on $p1 meters")

  /** p.age = 33, p.smart = true */
  def updateDynamic(field: String)(value: Any) =
    println(s"updating filed $field with value $value")

  /** p.callMeWhatEverYouWant(x = 1, y = true) */
  def applyDynamicNamed(method: String)(a: (String, Int), b: (String, Boolean)) =
    println(s"named dynamically $method ${a._1} ${a._2} ${b._1} ${b._2}")

}

object PersonTest extends App {
  val p = Person("Jim")
  p.jump
  p.go(3)
  p.age = 33
  p.smart = true
  p.callMeWhatEverYouWant(x = 1, y = true)
}
