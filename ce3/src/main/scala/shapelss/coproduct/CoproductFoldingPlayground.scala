package shapelss.coproduct

import shapeless.{:+:, CNil, Coproduct, Poly1}

object CoproductFoldingPlayground extends App {

  case class BadName(name: String)
  case class BadAge(age: Int)

  type E = BadName :+: BadAge :+: CNil

  object folder extends Poly1 {
    implicit def c1 = at[BadName](e => s"bad first name: ${e.name}")
    implicit def c2 = at[BadAge](e => s"bad age: ${e.age}")
  }

  def fold1(e: E): String = e.fold(folder)

  val x = fold1(Coproduct[E](BadName("jj")))
  pprint.log(x)
}
