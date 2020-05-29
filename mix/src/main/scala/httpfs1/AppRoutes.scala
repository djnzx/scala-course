package httpfs1

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import httpfs1.serv.{HelloWorld, Jokes}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request}

object AppRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      //                           extracting substring from path
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp     <- Ok(greeting)
        } yield resp
    }
  }

  def route1[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._

    /**
      * httpRoutes.of() expects PartialFunction: Request[F] => F[Response[F]]
      * and we will just combine them further <+>
      */
    val hr1: HttpRoutes[F] = HttpRoutes.of[F] { rq: Request[F] => rq match {
      case GET -> Root / "a" => Ok("Hi")
    }}

    hr1
  }

}
