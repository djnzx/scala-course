package schedule.eu

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import org.http4s.Status.{ClientError, Successful, TooManyRequests}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Method, Request}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.Locale
import scala.concurrent.duration.DurationLong
import scala.util.chaining.scalaUtilChainingOps

object Http {

  def mkHttpClient =
    BlazeClientBuilder[IO].resource

  private val url = uri"https://api.shedulem.e-u.edu.ua/lessons?q=%D0%B2%D0%B8%D0%BA%D0%BB.%20%D0%A0%D0%B8%D1%85%D0%B0%D0%BB%D1%8C%D1%81%D1%8C%D0%BA%D0%B8%D0%B9%20%D0%9E.%D0%AE."
  val rq = Request[IO](Method.GET, url)

  import model._

  def getData = mkHttpClient
    .flatMap(_.run(rq))
    .use {
      case Successful(rs)                                  => rs.as[RawResponse].map(_.some)
      case ClientError(rs) if rs.status == TooManyRequests => IO(None)
      case x                                               => IO(pprint.log(x)) >> IO.raiseError(new RuntimeException("wrong response"))
    }

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Http._
  import model._
//  import io.scalaland.chimney.dsl._

  def leftPad(x: Int, width: Int): String = s"%${width}d".formatted(x)
  def leftPad(x: Double, width: Int): String = leftPad(x.toInt, width)

  val DatePattern = """(\d{1,2})\s*(\S+)\s*(\d{2,4}).*""".r

  def handleDate(raw: String) = {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("uk"))
    val normalized = raw.trim match {
      case DatePattern(d, m, y) => y.length match {
          case 2 => s"$d $m 20$y"
          case _ => s"$d $m $y"
        }
      case _                    => sys.error("wrong range format")
    }
    LocalDate.parse(normalized, formatter)
  }

  test("handle date") {
    List(
      "07 вересня 2024 року.",
      "07 вересня 2024 р.",
      "07 вересня 2024",
      "07 вересня 24",
      "7 вересня 24",
      "7вересня24",
      "7 вересня24",
      "7вересня 24",
    )
      .map(handleDate)
      .foreach(x => pprint.pprintln(x))
  }

  val TimeRangePattern = raw"(\d{1,2}:\d{2})\s*-\s*(\d{1,2}:\d{2})".r

  def handleRange(raw: String) =
    raw match {
      case TimeRangePattern(from, to) =>
        val start = LocalTime.parse(from)
        val finish = LocalTime.parse(to)
        start -> {
          java.time.Duration.between(start, finish).toMinutes.minutes
        }
      case _                          => sys.error("wrong range format")
    }

  test("handle range") {
    val x = handleRange("12:17 - 13:17")
    pprint.log(x)
  }

  test("leftPad") {
    val x = leftPad(1.123, 6)
    pprint.log(x)
  }

  def normalizeDate(xs: List[String]) = xs match {
    case _ :: d0 :: r :: _ =>
      val d: LocalDate = handleDate(d0)
      handleRange(r) match { case (t, r) => d.atTime(t) -> r }
    case _                 => ???
  }

  def prettyQuotes(s: String) = {
    val op = '«'
    val cl = '»'
    def go(idx: Int, acc: List[Char], inside: Boolean): String = s.length match {
      case `idx` => acc.reverse.mkString
      case _     => (s(idx), inside) match {
          case ('"', true)  => go(idx + 1, cl :: acc, false)
          case ('"', false) => go(idx + 1, op :: acc, true)
          case (c, _)       => go(idx + 1, c :: acc, inside)
        }
    }
    go(0, Nil, false)
  }

  def extraSpace(raw: String) = raw.indexOf("«") match {
    case -1  => raw
    case idx =>
      val p1 = raw.substring(0, idx)
      val p2 = raw.substring(idx)
      p1 concat " " concat p2
  }

  def normalizeGroupName(x: String) =
    x.trim
      .pipe(prettyQuotes)
      .pipe(extraSpace)
      .replaceAll("\\s+", " ")
      .replaceAll(" ооф ", " оф ")

  sealed trait ST
  object ST {
    case object L extends ST
    case object P extends ST
  }

  def mapType(raw: String): ST = raw match {
    case "л"  => ST.L
    case "пр" => ST.P
    case _    => sys.error("wrong type: (not л/пр)")
  }

  def lType(xs: List[RawLesson]) = xs.headOption match {
    case Some(l)             => mapType(l.`type`)
    case None                => sys.error("different lessons put to the same time")
    case _ if xs.length != 1 => sys.error("different lessons put to the same time")
  }

  def isOffline(auditorium: String): Boolean =
    Some(auditorium)
      .map(_.toLowerCase)
      .filterNot(_.contains("zoom"))
      .filterNot(_.contains("google"))
      .isDefined

  def isOfflineAll(xs: List[RawLesson]) = xs.map(_.auditorium).toSet.headOption match {
    case Some(t) => isOffline(t)
    case _       => false
  }

  def represent(rs: RawResponse) =
    rs.lessons
      .groupBy(l => normalizeDate(l.date))
      .map { case ((d, r), lessons) =>
        (d, r, lType(lessons), isOfflineAll(lessons)) ->
          lessons.map(l => normalizeGroupName(l.group))
      }
      .toList
      .filter { case ((dt, _, _, _), _) => dt.isAfter(LocalDateTime.now) }
      .sortBy { case ((dt, _, _, _), _) => dt }

  test("one value") {
    getData
      .map(_.getOrElse(???))
      .map(represent)
//      .map(_.into[DataLine].transform)
//      .map(_.into[DataLineWattOnlyDetailed].transform)
      .flatMap(x => IO(pprint.log(x, width = 50)))
      .unsafeRunSync()
  }

}
