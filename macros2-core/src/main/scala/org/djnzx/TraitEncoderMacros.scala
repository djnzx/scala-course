package org.djnzx

import io.circe.Encoder
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object TraitEncoderMacros {

  def deriveTraitEncoder[A]: Encoder.AsObject[A] = macro deriveTraitEncoderImpl[A]

  def deriveTraitEncoderImpl[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[Encoder.AsObject[A]] = {
    import c.universe._

    val A = weakTypeOf[A]
    val sym: Symbol = A.typeSymbol

    if (!sym.isClass || !sym.asClass.isTrait) {
      c.abort(c.enclosingPosition, s"deriveTraitEncoder can only be used for traits, but got: ${A.typeSymbol.fullName}")
    }

    def jvmRelated(m: MethodSymbol): Boolean =
      m.name == TermName("hashCode") || m.name == TermName("toString") || m.name == TermName("equals")

    val pairs = sym.typeSignature
      .decls // declared, not inherited
      .collect {
        case m: MethodSymbol
            if m.isAbstract &&                // has no default implementation
              m.paramLists.flatten.isEmpty && // no parameters
              m.owner == sym &&
              m.isPublic &&
              !jvmRelated(m) => m
      }
      .map { m: MethodSymbol =>
        val nameLiteral = Literal(Constant(m.name.decodedName.toString))
        val returnType = A.member(m.name).typeSignatureIn(A) match {
          case MethodType(Nil, res)   => res
          case NullaryMethodType(res) => res
          case other                  => other
        }

        val encTpe = appliedType(typeOf[Encoder[_]].typeConstructor, returnType)
        val encVal = c.inferImplicitValue(encTpe)

        if (encVal == EmptyTree) {
          c.abort(c.enclosingPosition, s"Could not find an implicit for field '${m.name.decodedName}' of type io.circe.Encoder[$returnType]")
        }

        val methodGetter = Select(Ident(TermName("a")), m.name.toTermName)

        q"$nameLiteral -> $encVal.apply($methodGetter)"
      }

    val impl: Tree =
      q"""
        _root_.io.circe.Encoder.AsObject.instance[$A] { (a: $A) =>
          val fields: _root_.scala.List[(_root_.java.lang.String, _root_.io.circe.Json)] =
            _root_.scala.List(..$pairs)
          _root_.io.circe.JsonObject.fromIterable(fields)
        }
      """

    c.Expr[Encoder.AsObject[A]](impl)
  }

}
