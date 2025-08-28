package org.djnzx

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import io.circe.{Decoder, HCursor}

object TraitDecoderMacros {

  def deriveTraitDecoder[A]: Decoder[A] = macro deriveTraitDecoderImpl[A]

  def deriveTraitDecoderImpl[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[Decoder[A]] = {
    import c.universe._

    val A = weakTypeOf[A]
    val sym = A.typeSymbol

    if (!sym.isClass || !sym.asClass.isTrait) {
      c.abort(c.enclosingPosition,
        s"deriveTraitDecoder can only be used for traits, but got: ${A.typeSymbol.fullName}"
      )
    }

    def jvmRelated(m: MethodSymbol): Boolean =
      m.name == TermName("hashCode") || m.name == TermName("toString") || m.name == TermName("equals")

    val members: List[MethodSymbol] =
      sym.typeSignature.decls.collect {
        case m: MethodSymbol
          if m.isAbstract &&
            m.paramLists.flatten.isEmpty &&
            m.owner == sym &&
            m.isPublic &&
            !jvmRelated(m) => m
      }.toList

    // Prepare decoders and expressions: hc.get[ReturnType]("field")
    val decodedFields: List[(TermName, Tree, MethodSymbol, Type)] = members.map { m =>
      val fieldName = m.name.decodedName.toString
      val nameLit = Literal(Constant(fieldName))

      val returnType: Type = A.member(m.name).typeSignatureIn(A) match {
        case MethodType(Nil, res) => res
        case NullaryMethodType(res) => res
        case other => other
      }

      val decTpe = appliedType(typeOf[Decoder[_]].typeConstructor, returnType)
      val decVal = c.inferImplicitValue(decTpe)
      if (decVal == EmptyTree) {
        c.abort(c.enclosingPosition,
          s"Could not find an implicit for field '${m.name.decodedName}' of type io.circe.Decoder[$returnType]"
        )
      }

      val tmp = TermName(c.freshName(fieldName + "$"))
      val expr = q"hc.get[$returnType]($nameLit)($decVal)" // Decoder.Result[$returnType]
      (tmp, expr, m, returnType)
    }

    // Build the body of the anonymous implementation once
    val bodyVals: List[Tree] =
      decodedFields.map { case (tmp, _, m, tpe) =>
        q"override val ${m.name.toTermName}: $tpe = $tmp"
      }

    // Chain: df1.flatMap(tmp1 => df2.flatMap(tmp2 => ... Right(new A { ... })))
    val chainList: List[(TermName, Tree, Type)] =
      decodedFields.map { case (tmp, expr, _, tpe) => (tmp, expr, tpe) }

    def chain(xs: List[(TermName, Tree, Type)]): Tree = xs match {
      case Nil =>
        q"_root_.scala.Right(new $A { ..$bodyVals })"
      case (tmp, expr, tpe) :: tail =>
        q"$expr.flatMap(($tmp: $tpe) => ${chain(tail)})"
    }

    val impl: Tree =
      q"""
        _root_.io.circe.Decoder.instance[$A] { (hc: _root_.io.circe.HCursor) =>
          ${chain(chainList)}
        }
      """

    c.Expr[Decoder[A]](impl)
  }

}