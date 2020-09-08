package google

import scala.collection.immutable.TreeSet

object Task1 {
  case class Interval(min: Int, max: Int) {
    def contains(sub: Interval): Boolean = sub.min >= min && sub.max <= max
  }
  case class Input(int: Interval, name: String)
  case class Output(int: Interval, names: Seq[String])
  
  def mkIntervals(input: Seq[Interval]): Seq[Interval] = {
    val points: Vector[Int] = input
      .foldLeft(TreeSet.empty[Int]) { case (set, Interval(mn, mx)) => set ++ Set(mn, mx) }
      .toVector
    (0 to points.length-2).map(idx => Interval(points(idx), points(idx+1)))
  }

  /**
    * with empty sub-list implementation
    */
  def process_with_empty(input: Seq[Input]): Seq[Output] =
    mkIntervals(input.map(_.int))
      .map(subInt => Output(subInt, input.filter(_.int.contains(subInt)).map(_.name)))

  /**
    * without empty sub-list implementation
    */
  def process(input: Seq[Input]): Seq[Output] =
    mkIntervals(input.map(_.int))
      .flatMap { subInt =>
        input.view
          .filter(_.int.contains(subInt))
          .map(_.name)
          .toSeq
        match {
          case seq if seq.isEmpty => None
          case seq @ _            => Some(Output(subInt, seq))
        }
      }
  
}
