package gas104.spec

import cats.effect.IO
import gas104.Credentials
import gas104.Htttp._
import org.scalatest.funspec.AnyFunSpec

class MetersSpec extends AnyFunSpec {

  import Credentials._
  import cats.effect.unsafe.implicits.global

  describe("read meters - token required") {

    it("1") {
      mkHttpClient[IO]
        .use(obtainData[IO](sessionId104))
        .flatMap(representData[IO])
        .unsafeRunSync()
    }

  }

}
