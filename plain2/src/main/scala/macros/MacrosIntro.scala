package macros

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object MacrosIntro  {

  def hello(): Unit = macro hello_impl

  def hello_impl(c: whitebox.Context)(): c.Expr[Unit] = {
    import c.universe._

    val ex: c.universe.Expr[Unit] = reify {
      println("Hello World!")
    }
    ex
  }

  def print_param(x: Int, y: Double): Unit = macro print_param_impl

  def print_param_impl(c: whitebox.Context)(x: c.Expr[Any], y: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    val ex: c.universe.Expr[Unit] = reify {
      println(x.splice)
      println(y.splice)
    }
    ex
  }

  def debug(x: Any): Unit = macro debug_impl

  def debug_impl(c: whitebox.Context)(x: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    val rep: String = show(x.tree)
    val tree = Literal(Constant(rep))
    val expr = c.Expr[String](tree)

    reify {
      println(expr.splice + " = " + x.splice)
    }
  }
}

