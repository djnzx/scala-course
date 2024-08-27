package _sandbox

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Sandbox {

  def id[A](a: A): A = a
  def absurd[A]: A = ???

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Sandbox._

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
