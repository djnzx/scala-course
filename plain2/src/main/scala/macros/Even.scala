package macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object Even {

  def isEven(x: Int): Unit = macro isEvenMacro

  def isEvenMacro(c: blackbox.Context)(x: c.Tree): c.Tree = {
    import c.universe._

    q"""
       if ($x % 2 == 0)
         println("x is EVEN")
       else
         println("x is ODD")
       """
  }

}
