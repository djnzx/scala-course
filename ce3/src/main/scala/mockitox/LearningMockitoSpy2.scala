package mockitox

import org.mockito.scalatest.IdiomaticMockito
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LearningMockitoSpy2 extends AnyFunSuite with Matchers with IdiomaticMockito {

  class Calculator {
    def add0(a: Int, b: Int): Int = ???
    def add(a: Int, b: Int): Int = add0(a, b)
  }

  test("partial mock with Scala API") {

    val real0 = new Calculator
    val calc = spy(real0)

    calc.add0(1, 2) returns 103
//    103 willBe returned by calc.add0(1, 2)

    val result = calc.add(1, 2)

    result shouldBe 103
  }
}
