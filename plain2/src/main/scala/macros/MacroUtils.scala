package macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object MacroUtils {
  def createInstance[T]: GenericClass[T] = macro createInstanceImpl[T]

  def createInstanceImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[GenericClass[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]
    val params = q"${Literal(Constant("default"))}"
    val result = q"new GenericClass[$tpe]($params)"
    c.Expr[GenericClass[T]](result)
  }
}
