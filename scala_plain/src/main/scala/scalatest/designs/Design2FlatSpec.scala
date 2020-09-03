package scalatest.designs

import org.scalatest.flatspec._
import org.scalatest.matchers.should._

import scala.collection.mutable

/**
  * A good first step for teams wishing to move from xUnit to BDD,
  * FlatSpec's structure is flat like xUnit, so simple and familiar,
  * but the test names must be written in a specification style:
  * "X should Y," "A must B," etc.
  */
class Design2FlatSpec extends AnyFlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new mutable.Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new mutable.Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}
