package classnaming

import shapeless.Coproduct
import shapeless.ops.coproduct.Unifier

trait HasClassName[A] {
  def make(a: A): String
}

object HasClassName {

  implicit def coproductInstance[A <: Coproduct](
      implicit unifier: Unifier[A]
    ): HasClassName[A] =
    (a: A) => ScalaClassName.makeClassName(a.unify)

  object nonCoproductInstances {
    implicit def instance[A]: HasClassName[A] =
      (a: A) => ScalaClassName.makeClassName(a)
  }

}
