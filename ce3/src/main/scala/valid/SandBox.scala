package valid

import org.scalatest.Inside
import org.scalatest.Succeeded
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.reflect.runtime.universe.reify

class SandBox extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("1") {

    case class Person(id: Int, name: (String, String))
    val p = Person(13, "Jim"-> "Bim")

    val f1: model.Field[Int] = model.Field.make(p.id)
    val f2: model.Field[String] = model.Field.make(p.name._1)
    pprint.pprintln(f1)
    pprint.pprintln(f2)
    pprint.pprintln(reify(p.name._1))
  }

  test("2") {
    val l = m.Macros.currentLocation
    pprint.pprintln(l)



  }

}
