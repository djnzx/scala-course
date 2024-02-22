package valid.m

import scala.reflect.macros.whitebox

case class Location(filename: String, line: Int, column: Int)

object Macros {
  def currentLocation: Location = macro impl

  def impl(c: whitebox.Context): c.Expr[Location] = {
    import c.universe._

    val pos = c.macroApplication.pos
    val clz: c.universe.ModuleSymbol = c.mirror.staticModule("valid.m.Location")

    c.Expr(
      Apply(
        Ident(clz),
        List(
          Literal(Constant(pos.source.path)),
          Literal(Constant(pos.line)),
          Literal(Constant(pos.column))
        )
      )
    )
  }
}