package scalatest

import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable.ListBuffer

class LineSpecFlat extends AnyFlatSpec {

  case class Shared(builder: StringBuilder, buffer: ListBuffer[String])

  /**
    * shared component
    */
  def fixture = Shared(
    new StringBuilder("ScalaTest is "),
    new ListBuffer[String]
  )

  "Testing" should "be easy" in {
    val f = fixture
    f.builder.append("easy!")
    assert(f.builder.toString === "ScalaTest is easy!")
    assert(f.buffer.isEmpty)
    f.buffer += "sweet"
  }

  it should "be fun" in {
    val f = fixture
    f.builder.append("fun!")
    assert(f.builder.toString === "ScalaTest is fun!")
    assert(f.buffer.isEmpty)
  }
}
