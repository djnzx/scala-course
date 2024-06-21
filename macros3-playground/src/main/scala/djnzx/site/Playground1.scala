package djnzx.site

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.quoted.*

class Playground1 extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Core1.*
  
  test("macro 1 - unit") {
    // macro definition    splice syntax: Expr[A] => A
    // vvv                 vvvv              vvv
    inline def unit: Unit = ${ unitImpl }

    //         using macro implementation
    pprint.log(unit)
  }

  test("macro 2 - passing a parameter") {
    //                                                String => Expr[String]
    inline def length(s: String): Int = ${ lengthImpl('s) }

    val x = length("hello")

    /** {{{
     * length("Hello")
     *
     * Apply(
     *   fun = Ident("length"),
     *   args = List(
     *            Literal(Constant("hello")),
     *            ...
     *          )
     * )
     *
     * { myString.length() }
     *
     * Apply(
     *   fun = Select(Ident("myString"), "length"),
     *   args = List()
     * )
     *
     * Apply(
     *   fun = Select(Literal(Constant("hello")), "length"),
     *   args = List()
     * )
     *
     * Apply(
     *   fun = Ident("length"),
     *   args = List(Literal(Constant("hello")))
     * )
     * }}}
     */
    pprint.log(x)
  }

  test("macro 3 - only three basic things") {
    //
    //  1. function application
    //
    val x = "Hello, World".substring(0, 5)
    //  Apply {
    //    fun =Select(
    //      Literal(Constant("Hello, World")),
    //      "substring"
    //    )
    //    args = List(
    //      Literal(Constant(0)),
    //      Literal(Constant(5))
    //    )
    //  }
    val y = 12
    //  Block {
    //    stats = ValDef {
    //              name = x,
    //              tpt = TypeRef(ThisType(TypeRef(NoPrefix(),scala)),Int),
    //              rhs = Literal(Constant( 12 ))
    //    expr = Literal(Constant( () ))
    //  }
    def f(x: Int) = x + 1
    f(12)
    // https://macros.kitlangton.com/performing-the-macro-rites
    // https://macros.kitlangton.com/macro-tactics

  }

}