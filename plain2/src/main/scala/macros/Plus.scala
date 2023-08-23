package macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object Plus {

  def add2(x: Int): Int = macro add2_impl

  def add2_impl(c: blackbox.Context)(x: c.Expr[Int]) = {
    import c.universe._
//
//    val expr: c.universe.Apply = Apply(
//      Select(
//        Ident(TermName("x")),
//        TermName("$plus")
//      ),
//      List(
//        Literal(Constant(2))
//      )
//    )
//
//    expr
    q"$x + 2"
  }

}
