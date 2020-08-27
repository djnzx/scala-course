package composition

import cats._, cats.data._, cats.implicits._

object CatTh051 extends App {

  /**
    * http://eed3si9n.com/herding-cats/Coproduct.html
    */
  sealed trait XList[A]
  object XList {
    case class XNil[A]() extends XList[A]
    case class XCons[A](head: A, rest: XList[A]) extends XList[A]
  }

  val r: XList.XCons[Int] = XList.XCons(1, XList.XNil[Int]())
  type |:[+A1, +A2] = Either[A1, A2]
//  val ei: String |: Int = ???
  /** lifts "a" to Either[String, Int] actually Left */
  val a: String |: Int = Inject[String, String |: Int].inj("a")
  /** lifts 1 to Either[String, Int] actually Right */
  val one: String |: Int = Inject[Int, String |: Int].inj(1)
  
  val r1: Either[String, Int] = Inject[Int,    Either[String, Int]].inj(1)
  val r2: Either[String, Int] = Inject[String, Either[String, Int]].inj("aa")

  val ff: Either[String, Int] => Option[Int] = Inject[Int, Either[String, Int]].prj
}
