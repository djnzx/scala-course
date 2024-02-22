package valid

import scala.reflect.macros.blackbox
import scala.reflect.macros.whitebox

object model {

  case class Field[A](path: String, value: A)
  object Field {
    def make[A](value: A): Field[A] = macro FieldMacro.fieldMacro[A]
  }

  sealed trait FieldError
  case class Field1Error(msg: String, field: Field[_])   extends FieldError
  case class FieldsError(msg: String, fields: Field[_]*) extends FieldError

}

object FieldMacro {

  def fieldMacro[A: c.WeakTypeTag](c: whitebox.Context)(value: c.Expr[A]): c.Tree = {
    import c.universe._

    def mkFieldName(t: Tree): Tree = t match {
//      case Literal(Constant(str: String)) => q"$str"
      case Select(tree, TermName(propertyName)) =>
        c.info(c.enclosingPosition, s"Debug Info: ${reify(tree)}", force = true)
        c.info(c.enclosingPosition, s"Debug Info: ${show(tree)}", force = true)
        q"$propertyName"
      case _                                    => c.abort(c.enclosingPosition, "Only props are supported")
    }

    val fieldName: c.universe.Tree = mkFieldName(value.tree)
    q"""valid.model.Field($fieldName, $value)"""
  }

}
