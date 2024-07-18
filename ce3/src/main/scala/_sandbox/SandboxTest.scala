package _sandbox

import org.scalatest.Inside
import org.scalatest.Succeeded
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class SandboxTest extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  def method1: Int = ???
  def method2: String = ???

  class MyTuple(val x: Int, val name: String)

  def method3: (Int, String) = {
    return (33, "hello")
  }

  def method4: MyTuple = {
    return new MyTuple(33, "hello")
  }

  val r1: (Int, String) = method3
  val (x: Int, y: String) = method3



  test("1") {
  }

}
