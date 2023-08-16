package mockitox

import cats.implicits.catsSyntaxApplicativeId
import org.mockito.scalatest.AsyncMockitoSugar
import org.scalatest.Succeeded
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class LearningMockitoAsyncSpec extends AsyncFunSpec with Matchers with AsyncMockitoSugar with ScalaFutures {

  describe("Learning Mockito") {

    it("mocking trait with async response types") {

      trait Service {
        def f1(x: Int): Future[String]
      }

      val s: Service = mock[Service]

      when(s.f1(any)).thenReturn("aaa".pure[Future])
      when(s.f1(33)).thenReturn("aaa33".pure[Future])
      when(s.f1(44)).thenReturn("aaa44".pure[Future])

      val r33 = s.f1(33).futureValue
      val r44 = s.f1(44).futureValue
      val r55 = s.f1(55).futureValue

      verify(s).f1(33)
      verify(s).f1(44)
      verify(s).f1(55)

      r33 shouldBe "aaa33"
      r44 shouldBe "aaa44"
      r55 shouldBe "aaa"
    }

    it("mocking partially and verifying") {

      class Service {
        def logic(x: Int): Future[Int]  = if (x < 0) logic1(x) else logic2(x)
        def logic1(x: Int): Future[Int] = ???
        def logic2(x: Int): Future[Int] = ???
      }

      val service = mock[Service]
      when(service.logic(any)).thenCallRealMethod()

      service.logic(-3)
      service.logic(5)

      verify(service).logic1(-3)
      verify(service).logic2(5)

      Succeeded
    }

    it("mocking partially and verifying 2") {

      class Service {
        def logic(x: Int): Future[Int]  = if (x < 0) logic1(x) else logic2(x)
        def logic1(x: Int): Future[Int] = (-1000).pure[Future]
        def logic2(x: Int): Future[Int] = 1000.pure[Future]
      }

      val service = mock[Service]
      when(service.logic(any))
//        .thenReturn(999.pure[Future])
        .thenCallRealMethod()

      service.logic(-3)
      service.logic(5)

      verify(service).logic1(-3)
      verify(service).logic2(5)

      Succeeded
    }

  }

}
