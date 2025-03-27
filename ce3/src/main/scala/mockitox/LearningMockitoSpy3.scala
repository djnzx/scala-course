package mockitox

import org.mockito.IdiomaticMockito
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LearningMockitoSpy3 extends AnyFunSuite with Matchers with IdiomaticMockito {

  class Calculator {
    def add0(a: Int, b: Int): Int = ???
    def add(a: Int, b: Int): Int = add0(a, b)
  }

  /**
   * beware:
   * import org.mockito.scalatest.IdiomaticMockito DOESN'T WORK
   * import import org.mockito.IdiomaticMockito    WORKS WELL
   */

  test("partial mock with Java-ish API") {
    val real = new Calculator
    val calc = spy(real)

    org.mockito.Mockito.doReturn(113).when(calc).add0(1, 2)

    val outcome = calc.add(1, 2)

    outcome shouldBe 113
    calc.add0(1, 2) wasCalled once
  }

  test("partial mock with scala API") {
    val real = new Calculator
    val calc = spy(real)

    113 willBe returned by calc.add0(1, 2)

    val outcome = calc.add(1, 2)

    outcome shouldBe 113
    calc.add0(1, 2) wasCalled once
  }

}
