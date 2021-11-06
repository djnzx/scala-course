package scalatest

import org.scalatest.funspec.AnyFunSpec

import scala.collection.mutable.ListBuffer

class LineSpecFun extends AnyFunSpec {

  case class Shared(builder: StringBuilder, buffer: ListBuffer[String])

  /**
    * shared component
    * @return
    */
  def fixture = Shared(
    new StringBuilder("ScalaTest is "),
    new ListBuffer[String]
  )

  it("1") {
    val f = fixture
    f.builder.append("easy!")
    assert(f.builder.toString === "ScalaTest is easy!")
    assert(f.buffer.isEmpty)
    assert(1===1)
  }

}
