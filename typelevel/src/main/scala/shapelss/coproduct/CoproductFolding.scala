package shapelss.coproduct

import shapeless.:+:
import shapeless.CNil
import shapeless.Coproduct
import shapeless.Poly1
import shapeless.ops.coproduct.Inject

/** https://www.tobyhobson.com/posts/shapeless/coproducts/ */
object CoproductFolding extends App {

  /** data types */
  case class BadName(name: String)
  case class BadAge(age: Int)

  /** folder functions */
  val badNameMapper = (e: BadName) => s"bad first name: ${e.name}"
  val badAgeMapper = (e: BadAge) => s"bad age: ${e.age}"

  /** coproduct declaration */
  type E = BadName :+: BadAge :+: CNil
  object E {
    def apply[A](t: A)(implicit inj: Inject[E, A]): E = inj(t)
  }

  /** coproduct creation */
  val e1: E = Coproduct[E](BadName("jim"))
  val e1a: E = E(BadName("jim"))
  val e2: E = Coproduct[E](BadAge(-3))
  val e2a: E = E(BadAge(-3))

  /** actually it becomes a function: (E) => String , since Poly1 is a function A => B */
  object eCoproductFolder extends Poly1 {

    /** how to fold BadName */
    implicit def name = at[BadName](badNameMapper)

    /** how to fold BadAge */
    implicit def age = at[BadAge](badAgeMapper)
  }

  val eCoproductFolder2: Poly1 = Poly1
    .at[BadName](badNameMapper)
    .at[BadAge](badAgeMapper)
    .build

  val r1: String = e1.fold(eCoproductFolder)
  val r2: String = e2.fold(eCoproductFolder)

  println(r1)
  println(r2)

}
