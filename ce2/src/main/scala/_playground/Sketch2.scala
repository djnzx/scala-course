package _playground

object Sketch2 extends App {

  val a: AnyRef = ("Hello": AnyRef)
  val b: Object = ("Hello": Object)
  val c: AnyRef = "Hello".asInstanceOf[AnyRef]
  val d: Object = "Hello".asInstanceOf[Object]

}
