package mockitox

import org.mockito.IdiomaticMockito
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LearningMockitoSpy extends AnyFunSuite with Matchers with IdiomaticMockito {

  class Calculator {
    def add0(a: Int, b: Int): Int = a + b
    def add(a: Int, b: Int): Int = add0(a, b)

    def sub(a: Int, b: Int): Int = a - b
  }

  val realCalculator = new Calculator
  val spyCalculator = spy(realCalculator)

  test("1") {
    spyCalculator.add0(1, 2).returns(113)

    val x = spyCalculator.add(1, 2)
    pprint.log(x) // 113
  }

}
