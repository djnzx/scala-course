package mockitox

/** there are two different wrappers for `mockito-core`:
  *
  * https://mvnrepository.com/artifact/org.mockito/mockito-scala-scalatest
  * "org.mockito" %% "mockito-scala-scalatest" % "1.17.14"
  * "org.mockito" %% "mockito-scala"           % "1.17.14"
  * "org.mockito" %% "mockito-core"            % "4.8.1"
  * "ru.vyarus" % "generics-resolver"          % "3.0.3" https://github.com/xvik/generics-resolver
  * ---------------------------------------------------------------------------------------------------
  * has `org.mockito.scalatest.MockitoSugar` contains:
  *  - mock
  *  - when
  *  - any
  *  - verify, ...
  *  - we can write in Scala syntax: `when(service.addHidden(any)).thenAnswer((x: Int) => x + 100)`
  *
  * "org.scalatestplus" %% "mockito-4-11" % "3.2.16.0"     // "org.mockito" % "mockito-core" % "4.11.0"
  * ---------------------------------------------------------------------------------------------------
  * it has `org.scalatestplus.mockito.MockitoSugar`, contains:
  *  - mock
  *  everything else should be imported directly from mockito-core, like:
  *  - org.mockito.Mockito.when
  *  - org.mockito.ArgumentMatchers.any
  *  - org.mockito.Mockito.verify
  *  - we need to use ugly Java API: `when(service.addHidden(any)).thenAnswer(_.getArgument[Int](0) + 100)`
  */

// "org.scalatestplus" %% "mockito-4-11" % "3.2.16.0"
//import org.mockito.ArgumentMatchers.any
//import org.mockito.Mockito.verify
//import org.mockito.Mockito.when
//import org.scalatestplus.mockito.MockitoSugar

// "org.mockito" %% "mockito-scala-scalatest" % "1.17.14"
import org.mockito.scalatest.MockitoSugar
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class LearningMockitoSpec extends AnyFunSpec with Matchers with MockitoSugar {

  describe("Learning Mockito") {

    it("mocking underlying") {

      class Underlying {
        // complicated interaction we don't want to run in test. it's tested before
        def complex(x: Int): Int = sys.error("destroying the universe!")
      }

      class Outer(u: Underlying) {
        def make(x: Int): Int = u.complex(x)
      }

      val underlyingMocked: Underlying = mock[Underlying]

      val outer = new Outer(underlyingMocked)

      outer.make(13)

      /** the only thing we do care, that `underlying.complex` was called with `13` */
      verify(underlyingMocked).complex(13)
    }

    it("mocking partially and asserting") {

      class Service {
        def add10(x: Int): Int     = addHidden(x + 10)
        def add20(x: Int): Int     = addHidden(x + 20)
        def addHidden(x: Int): Int = ???
      }

      val service = mock[Service]

      when(service.add10(any)).thenCallRealMethod()
      when(service.add20(any)).thenCallRealMethod()

//      when(service.addHidden(any)).thenAnswer(_.getArgument[Int](0) + 100)
      when(service.addHidden(any)).thenAnswer((x: Int) => x + 100)

      service.add10(1) shouldBe 111
      service.add20(1) shouldBe 121
    }

    it("mocking partially and verifying") {

      class Service {
        def add10(x: Int): Int     = addHidden(x + 10)
        def add20(x: Int): Int     = addHidden(x + 20)
        def addHidden(x: Int): Int = ???
      }

      val service = mock[Service]
      when(service.add10(any)).thenCallRealMethod()
      when(service.add20(any)).thenCallRealMethod()

      service.add10(1)
      service.add20(1)

      verify(service).addHidden(11)
      verify(service).addHidden(21)
    }

    it("mocking partially and verifying 2") {

      class Service {
        def logic(x: Int): Int  = if (x < 0) logic1(x) else logic2(x)
        def logic1(x: Int): Int = ???
        def logic2(x: Int): Int = ???
      }

      val service = mock[Service]
      when(service.logic(any)).thenCallRealMethod()

      service.logic(-3)
      service.logic(5)

      verify(service).logic1(-3)
      verify(service).logic2(5)
    }

  }

}
