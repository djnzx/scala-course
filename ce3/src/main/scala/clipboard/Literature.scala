package clipboard

object Literature {

  def mapOnePf: PartialFunction[Int, Int] = {
    case 5            => 1
    case 6            => 2
    case 8            => 3
    case 10           => 4
    case 15           => 5
    case 17           => 6
    case 18           => 7
    case 27           => 8
    case x if x >= 29 => x - 20
  }

  def mapIt: Int => Option[Int] = mapOnePf.lift

  def remap(raw: String) =
    raw.split(",").map(_.trim).map(_.toInt).flatMap(mapIt).map(_.toString).mkString(", ")

}
