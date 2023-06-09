package classnaming.tests

import classnaming.HasClassName
import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.ops.coproduct.Inject
import shapeless.{:+:, CNil}

class HasClassNameSpec extends AsyncFunSpec with Matchers {

  describe("testing ToTag instances") {

    object DomainWithCoproduct {

      object Error1

      case class Error2(message: String)

      type MyCoproduct = Error1.type :+: Error2 :+: CNil

      object MyCoproduct {
        def apply[A](a: A)(implicit inj: Inject[MyCoproduct, A]): MyCoproduct = inj(a)
      }

    }

    /**
      * instances available automatically
      */
    it("coproduct instance") {
      import DomainWithCoproduct._

      val value1: MyCoproduct = MyCoproduct(Error1)
      val value2: MyCoproduct = MyCoproduct(Error2("boom!"))
      val toTag = implicitly[HasClassName[MyCoproduct]]

      toTag.make(value1) shouldEqual "Error1"
      toTag.make(value2) shouldEqual "Error2"
    }

    /**
      * instances need to be imported explicitly
      */
    it("non-coproduct instance - plain class") {
      val value1 = "boom !"

      import HasClassName.nonCoproductInstances._
      val toTag = implicitly[HasClassName[String]]

      toTag.make(value1) shouldEqual "String"
    }

    it("non-coproduct instance - case class") {
      case class MyError(x: String)
      val value1 = MyError("boom !")

      import HasClassName.nonCoproductInstances._
      val toTag = implicitly[HasClassName[MyError]]

      toTag.make(value1) shouldEqual "MyError"
    }

  }

}
