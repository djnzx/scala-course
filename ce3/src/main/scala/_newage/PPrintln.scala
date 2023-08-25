package _newage

import pprint.PPrinter

object PPrintln extends App {

  case class User(id: Int, name: String)
  object User

  val x = User(31, "Jim")

  pprint.pprintln(x, showFieldNames = false)


  (new PPrinter).pprintln(x)


}
