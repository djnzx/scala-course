package google.q

import scala.collection.immutable.TreeSet

object Task0 {

  case class Interval(min: Int, max: Int) {
    def contains(sub: Interval): Boolean = sub.min >= min && sub.max <= max
  }

  case class Input(int: Interval, name: String)

  case class Output(int: Interval, names: Seq[String])

  def mkIntervals(input: Seq[Interval]) =
    input
      .foldLeft(TreeSet.empty[Int]) { case (set, Interval(mn, mx)) =>
        set ++ Set(mn, mx)
      }
      .toVector match {
      case pts => (0 to pts.length - 2).map(i => Interval(pts(i), pts(i + 1)))
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
  def process(input: Seq[Input]) =
    mkIntervals(input.map(_.int))
      .flatMap { subInt =>
        input
          .withFilter(_.int.contains(subInt))
          .map(_.name)
        match {
          case Nil => None
          case seq => Some(Output(subInt, seq))
        }
      }
}
