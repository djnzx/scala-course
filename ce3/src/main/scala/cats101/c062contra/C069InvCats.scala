package cats101.c062contra

import cats.Monoid
import cats.implicits._

object C069InvCats extends App {

  // extracting string join monoid
  val ms: Monoid[String] = implicitly[Monoid[String]]

  // mapper symbol to string
  val mapperSynToStr: Symbol => String = (sm: Symbol) => sm.name
  // mapper string to symbol
  val mapperStrToSym: String => Symbol = (s: String) => Symbol(s)

  // construct Monoid[Symbol] from Monoid[String]
  implicit val monoidSymbol: Monoid[Symbol] = ms.imap(mapperStrToSym)(mapperSynToStr)

  val space = Symbol(" ")
  val r: Symbol = Symbol("a") |+| space |+| Symbol("few") |+| space |+| Symbol("words")
  println(r)
}
