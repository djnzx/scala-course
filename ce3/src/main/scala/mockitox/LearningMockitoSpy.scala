package mockitox

import org.mockito.IdiomaticMockito
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LearningMockitoSpy extends AnyFunSuite with Matchers with IdiomaticMockito {

  test("partial mock with Java-ish API") {
    class Calculator {
      def add0(a: Int, b: Int): Int = ???
      def add(a: Int, b: Int): Int = add0(a, b)
    }

    val realCalc = new Calculator
    val spyCalc = spy(realCalc)
    org.mockito.Mockito.doReturn(113).when(spyCalc).add0(1, 2)

    val outcome = spyCalc.add(1, 2)

    outcome shouldBe 113
    spyCalc.add0(1, 2) wasCalled once
  }

  test("partial mock with Scala API") {
    class Calculator {
      def add0(a: Int, b: Int): Int = ???
      def add(a: Int, b: Int): Int = add0(a, b)
    }

    val realCalc = new Calculator
    val spyCalc = spy(realCalc)

    113 willBe answered by spyCalc.add0(1, 2)

    val result = spyCalc.add(1, 2)

    result shouldBe 113
    spyCalc.add0(1, 2) wasCalled once
  }
}
