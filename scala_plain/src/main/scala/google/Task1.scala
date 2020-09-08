package google

import scala.collection.immutable.TreeSet

object Task1 extends App {
  case class Interval(min: Int, max: Int) {
    def contains(sub: Interval): Boolean = min >= sub.min && max <= sub.max
  }
  case class Input(int: Interval, name: String)
  case class Output(int: Interval, names: Seq[String])
  
  def mkIntervals(input: Seq[Interval]): Seq[Interval] = {
    val points: Vector[Int] = input
      .foldLeft(TreeSet.empty[Int]) { case (set, Interval(mn, mx)) => set ++ Set(mn, mx) }
      .toVector
    (0 to points.length-2).map(idx => Interval(points(idx), points(idx+1)))
  }
  
  def process(input: Seq[Input]): Seq[Output] = {
    val intervals: Seq[Interval] = mkIntervals(input.map(_.int))
    intervals.map(sub => Output(sub, input.filter(in => in.int.contains(sub)).map(_.name)))
  }
  
}
