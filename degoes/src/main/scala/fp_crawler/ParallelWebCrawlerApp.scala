package fp_crawler

import zio.console._

import zio._
import zio.blocking.Blocking

object ParallelWebCrawlerApp extends App {

  def getURL(url: URL): ZIO[Blocking, Exception, String] = {

    def effectBlocking[A](sideEffect: => A): IO[Throwable, String] = ???
    def getURLImpl(url: URL): String = scala.io.Source.fromURL(url.url)(scala.io.Codec.UTF8).mkString

    effectBlocking(getURLImpl(url)).refineOrDie { case e: Exception =>
      e
    }
  }

  final case class CrawlState[+E](visited: Set[URL], errors: List[E]) {
    def visitAll(urls: Set[URL]): CrawlState[E] = copy(visited = visited ++ urls)
    def logError[E1 >: E](e: E1): CrawlState[E1] = copy(errors = e :: errors)
  }

  def crawl[E](
      seeds: Set[URL], // initial list of url to visit
      router: URL => Set[URL], // function (hook) to terminate a process (return empty Set if you need to stop)
      processor: (URL, String) => ZIO[Any, E, Unit], // actual processing
    ): ZIO[Blocking, Nothing, List[E]] = { // representation Blocking -> shifts computation to the blocking TP
    // List[E] - list of Errors which we return if we need

    def loop(seeds: Set[URL], ref: Ref[CrawlState[E]]): ZIO[Blocking, Nothing, Unit] = {
      val zf1: ZIO[Blocking, Nothing, List[Set[URL]]] = ZIO.foreachParN(100)(seeds.toList) { seed =>
        {
          val f = for {
            html <- getURL(seed) // ZIO[Blocking, Exception, String]
            scraped = extractURLs(seed, html).toSet.flatMap(router) // Set[URL]
            either <- processor(seed, html).either // ZIO[Any, E, Unit].either
            newUrls <- ref.modify(state =>
              (scraped -- state.visited, { val s2 = state.visitAll(scraped); either.fold(s2.logError, _ => s2) }),
            ) // UIO[B]
          } yield newUrls
          f orElse ZIO.succeed(Set.empty[URL]) // UIO[A]
        }
      }
      val zf2: ZIO[Blocking, Nothing, Set[URL]] = zf1.map(_.toSet.flatten)
      val zf3: ZIO[Blocking, Nothing, Unit] = zf2.flatMap(newUrls => loop(newUrls, ref))

      zf3
    }

    val errors: ZIO[Blocking, Nothing, List[E]] = for {
      ref <- Ref.make(
        CrawlState(seeds, List.empty[E]),
      ) // ZIO[Any, Nothing, Ref[CrawlState[E]]] / UIO[Ref[CrawlState[E]]]
      _ <- loop(seeds, ref) // ZIO[Blocking, Nothing, Unit]
      state <- ref.get // ZIO[Any, Nothing, CrawlState[E]]      / UIO[CrawlState[E]]
    } yield state.errors

    errors // ZIO[Blocking, Nothing, List[E]]
  }

  /** A data structure representing a structured URL, with a smart constructor. */
  final case class URL private (parsed: io.lemonlabs.uri.Url) {
    import io.lemonlabs.uri._

    final def relative(page: String): Option[URL] =
      scala
        .util
        .Try(parsed.path match {
          case Path(parts) =>
            val whole = parts.dropRight(1) :+ page.dropWhile(_ == '/')
            parsed.withPath(UrlPath(whole))
          case _ => ???
        })
        .toOption
        .map(new URL(_))

    def url: String = parsed.toString

    override def equals(a: Any): Boolean = a match {
      case that: URL => this.url == that.url
      case _         => false
    }

    override def hashCode: Int = url.hashCode
  }

  object URL {
    import io.lemonlabs.uri._

    def make(url: String): Option[URL] =
      scala.util.Try(AbsoluteUrl.parse(url)).toOption match {
        case None         => None
        case Some(parsed) => Some(new URL(parsed))
      }
  }

  /** A function that extracts URLs from a given web page. */
  def extractURLs(root: URL, html: String): List[URL] = {
    val pattern = "href=[\"\']([^\"\']+)[\"\']".r

    scala
      .util
      .Try({
        val matches = (for (m <- pattern.findAllMatchIn(html)) yield m.group(1)).toList

        for {
          m <- matches
          url <- URL.make(m).toList ++ root.relative(m).toList
        } yield url
      })
      .getOrElse(Nil)
  }

  object test {
    val Home = URL.make("http://scalaz.org").get
    val Index = URL.make("http://scalaz.org/index.html").get
    val ScaladocIndex = URL.make("http://scalaz.org/scaladoc/index.html").get
    val About = URL.make("http://scalaz.org/about").get

    val SiteIndex =
      Map(
        Home -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        Index -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        ScaladocIndex -> """<html><body><a href="index.html">Home</a><a href="/about">About</a></body></html>""",
        About -> """<html><body><a href="home.html">Home</a><a href="http://google.com">Google</a></body></html>""",
      )

    val Processor: (URL, String) => IO[Unit, List[(URL, String)]] =
      (url, html) => IO.succeed(List(url -> html))
  }

//    def run(args: List[String]): ZIO[Console, Nothing, Int] = {
//
//      val app: ZIO[Console, Nothing, Unit] = for {
//        _ <- putStrLn("Hello World!")
//      } yield ()
//
//      app.fold(_ => 1, _ => 0)(CanFail)
//    }
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val app: ZIO[Console, Nothing, Unit] = for {
      _ <- putStrLn("Hello World")
    } yield ()
    app.fold(_ => ExitCode.failure, _ => ExitCode.success)(CanFail)
  }
}
