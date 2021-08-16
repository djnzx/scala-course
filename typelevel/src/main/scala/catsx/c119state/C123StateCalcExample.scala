package catsx.c119state

object C123StateCalcExample extends App {

  sealed trait Cx
  final case class Num(v: Int) extends Cx
  final case class Op(name: String) extends Cx

  def parse(s: String): Option[Cx] =
    s.toIntOption.map(Num)
      .orElse(Option.when("+-*/".contains(s))(Op(s)))

  val entered = "1 2 + 3 *"
    .split(" ")
    .flatMap(parse)

}
