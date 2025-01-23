package _sandbox

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.collection.Searching
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object Sandbox {

  def id[A](a: A): A = a
  def absurd[A]: A = ???

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Sandbox._

  test("lists") {

    val xx: Nothing = throw new Exception()

    val q: Int = xx
    val z: String = xx

    val x: Option[Int] = Some(4)

    x match {
      case Some(value) => ???
      case None        => ???
    }

    val l1 = List(1, 2, 3)

    val l2s = List(
      List(10, 11, 12), // l2
      List(20, 21, 22),
      List(30, 31, 32),
    )

    // 1 2 3 10 11 12
    // 1 2 3 20 21 22
    // 1 2 3 30 31 32

    val ls3 = l2s.map(l1 ++ _)
    pprint.log(ls3)
  }

  test("ldt") {
    val x = LocalDateTime.now.atZone(ZoneOffset.of("+3"))
    val s = x.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    println(s)
  }

  test("convert") {
    val x: Int = "abc".toInt               // or Exception
    val y: Option[Int] = "abc".toIntOption // never throw Exception
  }

  test("555") {
    val x: Int = 5
    val y: Int = 5

    println(x == y)

    case class Pizza(name: String)

    object Pizza {
      val errorMessage = "Pizza must be 10+ size"
    }

//    p1.errorMessage
    val m = Pizza.errorMessage

    val p1 = Pizza("M")
    val p2 = Pizza("M")

    println(p1 == p2)      // true
    println(p1.equals(p2)) // true
    println(p1.eq(p2))     // false
  }

  test("allmatch") {
    val x = Seq.empty[Int].forall(_ > 0)
    pprint.log(x)
  }

  test("5") {
    val x = Seq.empty[Int].forall(_ > 0)
    pprint.log(x) // true
    val y = Seq.empty[Int].exists(_ > 0)
    pprint.log(y) // false
  }

  test("2") {
    Seq.empty[Int] match {
      case Seq(1) => ???
      case Seq()  => println("empty!")
      case _      => ???
    }
  }

  test("3") {
    Set.empty[Int] match {
      case Seq(1) => ???
      case Seq()  => println("empty!")
      case _      => ???
    }
  }

  test("123") {
    case class Person(age: Int, name: String)
    def getAge = Future(123)
    def getName = Future("Jim")

    val fp: Future[Person] =
      getName.flatMap(n =>
        getAge.map(a =>
          Person(a, n)
        )
      )
    val p: Person = Await.result(fp, 10.seconds)

  }

  test("11") {
    pprint.log(math.acos(0.96))
    pprint.log(math.sin(math.acos(0.96)))
    pprint.log(math.acos(1))
    pprint.log(math.sin(math.acos(1)))
    pprint.log(math.acos(0.95))
    pprint.log(math.sin(math.acos(0.95)))
    pprint.log(math.acos(0.65))
    pprint.log(math.sin(math.acos(0.65)))
  }

  test("0") {
    def isScala212a(v: Option[(Long, Long)]): Boolean =
      v.exists(_._1 == 2) && v.exists(_._2 == 12)

    def isScala212(v: Option[(Long, Long)]): Boolean =
      v.exists { case (a, b) => a == 2 && b == 12 }
  }

  test("1") {
    id(1) shouldBe 1
  }

  test("exception syntax") {
    an[NumberFormatException] shouldBe thrownBy("q".toInt)
  }

  test("exception syntax with details") {
    1 shouldEqual 1
    val x: NumberFormatException = the[NumberFormatException] thrownBy {
      "q".toInt
    }
    x.getClass shouldBe classOf[NumberFormatException]
    x.getMessage shouldBe "For input string: \"q\""
  }

}
