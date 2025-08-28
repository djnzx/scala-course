package org.djnzx

import io.circe._
import org.djnzx.TraitDecoderMacros.deriveTraitDecoderImpl
import org.djnzx.TraitEncoderMacros.deriveTraitEncoderImpl
import scala.reflect.macros.blackbox

object TraitCodecMacros {

  def deriveTraitCodec[A]: Codec.AsObject[A] = macro deriveTraitCodecImpl[A]

  def deriveTraitCodecImpl[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[Codec.AsObject[A]] = {
    import c.universe._
    val enc = deriveTraitEncoderImpl[A](c)
    val dec = deriveTraitDecoderImpl[A](c)
    val impl = q"_root_.io.circe.Codec.AsObject.from($dec, $enc)"

    c.Expr[Codec.AsObject[A]](impl)
  }
}
