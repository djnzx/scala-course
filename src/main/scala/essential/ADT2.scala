package essential

object ADT2 extends App {

  sealed trait Feline {
    def dinner: Food = this match {
      case Lion() => Antelope
      case Tiger() => TigerFood
      case Panther() => Licorice
      case Cat(food) => CatFood(food)
    }
  }
  final case class Lion() extends Feline
  final case class Tiger() extends Feline
  final case class Panther() extends Feline
  final case class Cat(favouriteFood: String) extends Feline

  sealed trait Food
  case object Antelope extends Food
  case object TigerFood extends Food
  case object Licorice extends Food
  final case class CatFood(food: String) extends Food

  object Diner {
    def dinner(feline: Feline): Food =
      feline match {
        case Lion() => Antelope
        case Tiger() => TigerFood
        case Panther() => Licorice
        case Cat(food) => CatFood(food)
      }
  }

}
