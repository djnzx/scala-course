package google

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.language.implicitConversions
import pprint.{pprintln => pln}

/**
  * nested rectangles
  * find the longest nested chain
  * from the given set
  * (101, 1), (102, 2), (103, 3),
  * (4, 204), (5, 205),
  * (11,11), (12,12), (13,13), (14,14),
  * 
  * List((14, 14), (13, 13), (12, 12), (11, 11))
  */
object Task1NestedRectangles {
  type R = (Int, Int) // rectangle, width and height

  def gt(bg: R, sm: R) = bg._1 > sm._1 && bg._2 > sm._2 
  
  val rectangles: List[R] = List(
    (101, 1), (102, 2), (103, 3),
    (4, 204), (5, 205),
    (11,11), (12,12), (13,13), (14,14),
  )
  
  def allSmallerTo(rs: List[R], bigger: R) = rs.filter(gt(bigger, _))

  def inscribed(rs: List[R]): List[List[R]] = rs match {
    case Nil => List(Nil)
    case _   => rs.flatMap { bg => inscribed(allSmallerTo(rs, bg)).map(bg :: _) }
  }
  
  def longest(rs: List[List[R]]) = rs.maxBy(_.length)
}

class NestedRectanglesSpec extends AnyFunSpec with Matchers {
  import Task1NestedRectangles._

  it("fit") {
    val t = Seq(
      ((3,3), (1,1)),
      ((3,3), (2,1)),
      ((3,3), (1,2)),
      ((3,3), (1,1)),
    ).map(_->true)
    val f = Seq(
      ((3,3), (3,1)),
      ((3,3), (3,3)),
      ((3,3), (4,3)),
      ((3,3), (4,4)),
    ).map(_->false)
    for {
      ((bg, sm), r) <- (t ++ f).toMap
    } gt(bg, sm) shouldEqual r
  }

  it("all") {
    val r = inscribed(rectangles)
    r should contain theSameElementsAs List(
      List((101, 1)),
      List((102, 2), (101, 1)),
      List((103, 3), (101, 1)),
      List((103, 3), (102, 2), (101, 1)),
      List((4, 204)),
      List((5, 205), (4, 204)),
      List((11, 11)),
      List((12, 12), (11, 11)),
      List((13, 13), (11, 11)),
      List((13, 13), (12, 12), (11, 11)),
      List((14, 14), (11, 11)),
      List((14, 14), (12, 12), (11, 11)),
      List((14, 14), (13, 13), (11, 11)),
      List((14, 14), (13, 13), (12, 12), (11, 11))
    )
  }

  it("longest") {
    val r = inscribed(rectangles)
    longest(r) shouldEqual List((14, 14), (13, 13), (12, 12), (11, 11))
  }
}