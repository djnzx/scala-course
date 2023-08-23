package macros

import java.util.UUID
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object MakerMacros {

  def createInstanceWithParams[A](args: UUID*): A = macro createInstanceWithParamsImpl[A]

  def createInstanceWithParamsImpl[A: c.WeakTypeTag](c: blackbox.Context)(args: c.Expr[UUID]*): c.Expr[A] = {
    import c.universe._

    val classType: c.universe.Type = weakTypeOf[A]
    println(classType)

    if (classType.typeSymbol.isClass && classType.typeSymbol.asClass.isCaseClass) {
      val constructorArgs: Seq[c.universe.Tree] = args.map(arg => q"$arg")
      c.Expr[A](q"new $classType(..$constructorArgs)")
    } else {
      c.abort(c.enclosingPosition, s"Cannot create an instance of non-case class: ${classType.typeSymbol.name}")
    }
  }

}
