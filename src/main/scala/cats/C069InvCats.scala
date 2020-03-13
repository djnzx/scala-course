package cats

import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.invariant._ // for imap
import cats.syntax.semigroup._ // for |+|

object C069InvCats extends App {

  // extracting string join monoid
  val ms: Monoid[String] = implicitly[Monoid[String]]

  // mapper symbol to string
  val mapperSynToStr: Symbol => String = (sm: Symbol) => sm.name
  // mapper string to symbol
  val mapperStrToSym: String => Symbol= (s: String) => Symbol(s)

  // construct Monoid[Symbol] from Monoid[String]
  implicit val monoidSymbol: Monoid[Symbol] = ms.imap(mapperStrToSym)(mapperSynToStr)

  val space = Symbol(" ")
  val r: Symbol = 'a |+| space |+| 'few |+| space |+| 'words
  println(r)
}
