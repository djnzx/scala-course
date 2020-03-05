package scalatest

import org.scalatest._
import prop._
import scala.collection.immutable._

/**
  * PropSpec is perfect for teams that want to write tests exclusively
  * in terms of property checks; also a good choice for writing the occasional
  * test matrix when a different style trait is chosen as the main unit testing style.
  */
class Design6PropSpec extends PropSpec with TableDrivenPropertyChecks with matchers.should.Matchers {

  val examples =
    Table(
      "set",
      BitSet.empty,
//      HashSet.empty[Int],
//      TreeSet.empty[Int]
    )

  property("an empty Set should have size 0") {
    forAll(examples) { set =>
      set.size should be (0)
    }
  }

  property("invoking head on an empty set should produce NoSuchElementException") {
    forAll(examples) { set =>
      a [NoSuchElementException] should be thrownBy { set.head }
    }
  }
}
