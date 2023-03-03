package app

import cats.Monad
import java.util
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import scala.util.Random

class TestRoutes[F[_]: Monad] extends Http4sDsl[F] {

  val m = new util.HashMap[Int, String](10)
  (0 to 9).foreach(n => m.put(n, Random.alphanumeric.take(20).toList.mkString))

  def routes = HttpRoutes.of[F] {
    case GET -> Root / "fix"  => Ok("Hello")
    case GET -> Root / "0"    => Ok(m.get(0))
    case GET -> Root / "hash" => Ok(m.get(Random.nextInt(10)))
  }

}

object TestRoutes {
  def apply[F[_]: Monad] = new TestRoutes[F].routes
}
