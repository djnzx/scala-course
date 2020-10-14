package toptal.q2

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object T1 {

  def parse(s: String) = s.toSeq match {
    case l => (
      l.takeWhile(_.isLetter).mkString,
      l.dropWhile(_.isLetter).takeWhile(_.isDigit).mkString.toInt)
  }

  def solution(t: Array[String], r: Array[String]): Int =
    (t zip r)
      .map { case (t, r) => (parse(t), r) }
      .groupBy(_._1._2)
    match {
      case d => (d.count(_._2.forall(_._2=="OK")).toDouble*100 / d.size).toInt
    }
}

class T1Spec extends AnyFunSpec with Matchers {
  import T1._
  
  it("1") {
    parse("test35c") shouldEqual ("test", 35)
    parse("test1") shouldEqual ("test", 1)
  }
  
  it("2") {
    val t = Array("test1a", "test2", "test1b", "test1c", "test3")
    val r = Array("Wrong answer", "OK", "Runtime error", "OK", "Time limit exceeded")
    solution(t, r) shouldEqual 33
  }
}