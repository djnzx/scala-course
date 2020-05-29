package catsx

import cats.Monad
import cats.syntax.flatMap._
import cats.syntax.functor._

object C151ProductFiaFlatMap extends App {

  def product[M[_]: Monad, A, B](x: M[A], y: M[B]): M[(A, B)] =
    x.flatMap(a => y.map(b => (a, b)))

}
