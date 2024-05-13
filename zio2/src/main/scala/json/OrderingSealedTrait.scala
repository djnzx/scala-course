package json

import magnolia1.{Magnolia, SealedTrait}

object OrderingSealedTrait {

  type Typeclass[A] = Ordering[A]

  def split[A](ctx: SealedTrait[Ordering, A]): Ordering[A] =
    (x: A, y: A) =>
      ctx.split(x) { sub =>
        sub.typeclass.compare(sub.cast(x), sub.cast(y))
      }

  def gen[A]: Ordering[A] = macro Magnolia.gen[A]

}
