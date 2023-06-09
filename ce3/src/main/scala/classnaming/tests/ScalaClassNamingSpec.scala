package classnaming.tests

import classnaming.ScalaClassName.makeClassName
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ScalaClassNamingSpec extends AnyFunSpec with Matchers {

  object Error3

  object Domain2 {
    object Error2

    object SubDomain {
      object Error4
      case class Error5(x: String)
    }
  }

  /**
    * thanks for Scala flexibility
    * we can create objects and classes in a variety ways
    * - object
    * - object inside the class
    * - object inside the object
    * - ...
    *
    * after this investigation, we come up with the conclusion we can rely only on
    * {{{
    *   .getClass.getName
    * }}}
    * and parse it as we want
    */
  // object in the separate file
  val f = Error0
  // object inside another object
  val b = Domain1.Error1
  // object inside another class
  val c = Error3
  // ...
  val d = Domain2.Error2
  // ...
  val e = Domain2.SubDomain.Error4
  val g = Domain2.SubDomain.Error5("boom!")
  val h = Error6("boom!")

  val ac = f.getClass
  val bc = b.getClass
  val cc = c.getClass
  val dc = d.getClass
  val ec = e.getClass
  val gc = g.getClass
  val hc = h.getClass

  describe("scala class naming - investigation") {

    case class Error7(x: String)
    case class Error8(x: String)
    val i = Error7("boom!")
    val j = Error8("boom!")
    val ic = i.getClass
    val jc = j.getClass

    val prefix = "classnaming.tests."

    it(".getName should work for any combination ;)") {
      ac.getName shouldEqual prefix + "Error0$"
      bc.getName shouldEqual prefix + "Domain1$Error1$"
      cc.getName shouldEqual prefix + "ScalaClassNamingSpec$Error3$"
      dc.getName shouldEqual prefix + "ScalaClassNamingSpec$Domain2$Error2$"
      ec.getName shouldEqual prefix + "ScalaClassNamingSpec$Domain2$SubDomain$Error4$"
      gc.getName shouldEqual prefix + "ScalaClassNamingSpec$Domain2$SubDomain$Error5"
      hc.getName shouldEqual prefix + "Error6"
      ic.getName shouldEqual prefix + "ScalaClassNamingSpec$Error7$1"
      jc.getName shouldEqual prefix + "ScalaClassNamingSpec$Error8$1"
    }

    it(".getSimpleName works only for: :(") {
      ac.getSimpleName shouldEqual "Error0$"
      bc.getSimpleName shouldEqual "Error1$"
      cc.getSimpleName shouldEqual "Error3$"
    }

//    it(".getSimpleName fails for: case1 :(") {
//      an[InternalError] shouldBe thrownBy {
//        dc.getCanonicalName
//      }
//    }
//
//    it(".getSimpleName fails for: case2 :(") {
//      an[InternalError] shouldBe thrownBy {
//        ec.getCanonicalName
//      }
//    }
//
//    it(".getSimpleName fails for: case3 :(") {
//      an[InternalError] shouldBe thrownBy {
//        gc.getCanonicalName
//      }
//    }

    it(".getCanonicalName works only for some cases and has different implementations :(") {
      ac.getCanonicalName shouldEqual prefix + "Error0$"
      bc.getCanonicalName shouldEqual prefix + "Domain1.Error1$"
      cc.getCanonicalName shouldEqual prefix + "ScalaClassNamingSpec.Error3$"
    }

//    it(".getCanonicalName fails for: case1 :(") {
//      an[InternalError] shouldBe thrownBy {
//        dc.getCanonicalName
//      }
//    }
//
//    it(".getCanonicalName fails for: case2 :(") {
//      an[InternalError] shouldBe thrownBy {
//        ec.getCanonicalName
//      }
//    }
//
//    it(".getCanonicalName fails for: case3 :(") {
//      an[InternalError] shouldBe thrownBy {
//        gc.getCanonicalName
//      }
//    }

  }

  describe("scala class name implementation") {

    /** case classes defined inside the lambda have different naming conventions */
    case class Error7(x: String)
    case class Error8(x: String)
    val i = Error7("boom!")
    val j = Error8("boom!")

    it("mkClassName should work for all possible combinations") {
      makeClassName(f) shouldEqual "Error0"
      makeClassName(b) shouldEqual "Error1"
      makeClassName(c) shouldEqual "Error3"
      makeClassName(d) shouldEqual "Error2"
      makeClassName(e) shouldEqual "Error4"
      makeClassName(g) shouldEqual "Error5"
      makeClassName(h) shouldEqual "Error6"
      makeClassName(i) shouldEqual "Error7"
      makeClassName(j) shouldEqual "Error8"
    }

  }

}
