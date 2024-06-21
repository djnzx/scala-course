package djnzx.yt

import scala.quoted.* // <==

/**
 * 0. Macro should be in another file
 */
object MacroTool {

  val hammer = "1.2345"

  /** 1A. macro definition with `inline`
    * TODO: what about inline parameters?
    */
  inline def debug(value: String): String =
    /** 1B. wiring with implementation
      * value being converted to Expr[Value]
      */
    ${ debugImpl('value) }

  /** 2. macro implementation
    * actually just a function Expr => Expr
    */
  def debugImpl(value: Expr[String])(using Quotes): Expr[String] = {
    import quotes.reflect.* // <==

    value.asTerm.underlyingArgument match {
      case Ident(name) =>
        val nameExpr: Expr[String] = Expr(name) // name of identifier
        '{ "identifier given: " + $nameExpr + " = " + $value }
      case Literal(_)  =>
        '{ "literal given: " + $value }
      case _           =>
        report.errorAndAbort(s"something strange given: ${value.asTerm}")
        value
    }
  }

  def inspect(x: Expr[Any])(using Quotes): Expr[Any] = {
    println(x.show)
    x
  }

}
