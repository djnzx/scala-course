package ninetynine

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

abstract class NNSpec extends AnyFunSpec with Matchers with BeforeAndAfterAll {
  
  def println[A](a: A) = pprint.pprintln(a)
  
}
