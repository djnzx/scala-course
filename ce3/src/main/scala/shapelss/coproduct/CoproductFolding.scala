package shapelss.coproduct

import shapeless.:+:
import shapeless.CNil
import shapeless.Coproduct
import shapeless.Poly1
import shapeless.ops.coproduct.Inject

/** https://www.tobyhobson.com/posts/shapeless/coproducts/ */
object CoproductFolding extends App {

  /** data types, domain description */
  case class BadName(name: String)
  case class BadAge(age: Int)
  case object TokenError

  /** coproduct declaration */
  type E = BadName :+: BadAge :+: TokenError.type :+: CNil

  /** coproduct creation, way 1 */
  val e1: E = Coproduct.apply[E](BadName("jim"))
  val e2: E = Coproduct[E](BadAge(-3))
  val e3: E = Coproduct[E](TokenError)

  object E {
    def apply[A](
        a: A
      )(
        implicit inj: Inject[E, A]
      ): E = inj(a)

  }

  /** coproduct creation, way 2 */
  val e1a: E = E(BadName("jim"))
  val e2a: E = E(BadAge(-3))
  val e3a: E = E(TokenError)

  /** at the same moment on the app
    * we need to remap it somehow: E => A
    * in this case we fold everything to String
    */
  object DomainMappers {
    val badNameMapper    = (e: BadName) => s"bad first name: ${e.name}"
    val badAgeMapper     = (e: BadAge) => s"bad age: ${e.age}"
    val tokenErrorMapper = (_: TokenError.type) => "Token Error!"
  }

  /** actually it becomes a function: E => String , since Poly1 is a function A => B
    * simply said in the followinf object we need to put all possible implementations A => B
    */
  object eCoproductFolder extends Poly1 {

    import DomainMappers._

    /** how to fold BadName */
    implicit def name = at[BadName](badNameMapper)

    /** how to fold BadAge */
    implicit def age = at[BadAge](badAgeMapper)

    /** how to deal with TokenError */
    implicit def tokenError = at[TokenError.type](tokenErrorMapper)
  }

  object Approach1 {
    def fold1(e: E): String = e.fold(eCoproductFolder)

    val r1: String = fold1(e1)
    val r2: String = fold1(e2)
    val r3: String = fold1(e3)

    def go = {
      println(r1)
      println(r2)
      println(r3)
    }

  }

  object Approach2 {
    import DomainMappers._

    val eCoproductFolder2 = Poly1
      .at[BadName](badNameMapper)
      .at[BadAge](badAgeMapper)
      .at[TokenError.type](tokenErrorMapper)
      .build

    // TODO: doesn't work
    // def fold2(e: E) = e.fold(eCoproductFolder2)

//    val r1 = fold2(e1)
//    val r2 = fold2(e2)
//    val r3 = fold2(e3)
//
//    def go = {
//      println(r1)
//      println(r2)
//      println(r3)
//    }

  }

  Approach1.go

}
