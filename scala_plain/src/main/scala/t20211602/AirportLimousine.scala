package t20211602

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.chaining.scalaUtilChainingOps

object AirportLimousine extends App {

  case class Pt(x: Int, y: Int) {
    def r = copy(x = x + 1)
    def d = copy(y = y + 1)
    def l = copy(x = x - 1)
    def u = copy(y = y - 1)
    def isAirport(road: Road) = this == Pt.airport(road)
    def isCity = this == Pt.city
  }
  object Pt {
    def city = Pt(0, 0)
    def airport(road: Road) = Pt(road(0).length-1, road.length-1)
  }
  type Road = Array[Array[Int]]
  def markVisited(road: Road, pt: Pt) = road(pt.y)(pt.x).tap(_ => road(pt.y)(pt.x) = 0)
  def markUnvisited(road: Road, pt: Pt, value: Int) = road(pt.y)(pt.x) = value
  def isOnRoad(road: Road, pt: Pt) = pt.y >= 0 && pt.y < road.length && pt.x >= 0 && pt.x < road(pt.y).length
  def isObstacle(road: Road, pt: Pt) = road(pt.y)(pt.x) == -1
  def isValidPt(road: Road, pt: Pt) = isOnRoad(road, pt) && !isObstacle(road, pt)
  def next1(road: Road, pt: Pt) = Seq(pt.r, pt.d).filter(isValidPt(road, _))
  def next2(road: Road, pt: Pt) = Seq(pt.l, pt.u).filter(isValidPt(road, _))
  def printMe(road: Road) = road.map(_.mkString(" ")).mkString("\n").pipe(println)
  def clone(road: Road) = road.map(_.clone)
  /** road to the airport */
  def toAirport(road: Road, pt: Pt, sum: Int): Option[(Int, Road)] = {
    val at = markVisited(road, pt)
    val sum2 = sum + at
    if (pt.isAirport(road))
      clone(road)
        .tap(_ => markUnvisited(road, pt, at))
        .pipe(Some(sum2, _))
    else 
      next1(road, pt)
        .flatMap(toAirport(road, _, sum2))
        .maxByOption { case (sum, _) => sum }
        .tap(_ => markUnvisited(road, pt, at))
  }
  /** road to the city */
  def toCity(road: Road, pt: Pt, sum: Int): Option[(Int, Road)] = {
    val at = markVisited(road, pt)
    val sum2 = sum + at
    if (pt.isCity)
      clone(road)
        .tap(_ => markUnvisited(road, pt, at))
        .pipe(Some(sum2, _))
    else 
      next2(road, pt)
        .flatMap(toCity(road, _, sum2))
        .maxByOption { case (sum, _) => sum }
        .tap(_ => markUnvisited(road, pt, at))
  }
  /** combination */
  def max(road: Road) = 
    toAirport(road, Pt.city, 0)
      .flatMap { case (sum1, road) => toCity(road, Pt.airport(road), sum1) }
      .map { case (max, _) => max }
      .getOrElse(0)
}

class App1Spec extends AnyFunSpec with Matchers {
  
  describe("a") {
    import AirportLimousine._
    val road = Array(
      Array(1,1,1),
      Array(3,2,1),
      Array(5,1,1),
    )
    it("isOnRoad") {
      val x = Seq(
        (road, Pt(0, 0)) -> true,
        (road, Pt(2, 2)) -> true,
        (road, Pt(3, 3)) -> false,
        (road, Pt(3, 2)) -> false,
        (road, Pt(2, 3)) -> false,
      )
      for {
        ((road, pt), exp) <- x
      } isOnRoad(road, pt) shouldEqual exp
    }
    it("nextTo") {
      next1(road, Pt(0, 0)) shouldEqual Seq(Pt(1,0), Pt(0,1)) 
      next1(road, Pt(2, 1)) shouldEqual Seq(Pt(2,2)) 
      next1(road, Pt(1, 2)) shouldEqual Seq(Pt(2,2)) 
      next1(road, Pt(2, 2)) shouldEqual Seq() 
    }
    it("print") {
      printMe(road)
    }
    it("isAirPort") {
      Pt(2, 2).isAirport(road) shouldEqual true
      Pt(3, 2).isAirport(road) shouldEqual false
      Pt(1, 2).isAirport(road) shouldEqual false
    }
    it("v0") {
      val r = toAirport(road, Pt.city, 0)
      r.foreach { case (sum, map) =>
        println(sum)
        printMe(map)
      }
    }
    it("v1") {
      println(max(road))
    }
  }
}
