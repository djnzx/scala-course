package shapelss.hlists

import shapeless._

object HListAsAReturnType1 extends App {

  def doIt(x: Int): HList = x match {
    case 1 => 1 :: HNil
    case 2 => 1 :: "two" :: HNil
    case 3 => 1 :: "two" :: '3' :: HNil
    case 4 => 1 :: "two" :: '3' :: 3.14 :: HNil
    case _ => HNil
  }

  doIt(0) match {
    case (a: Int) :: (b: String) :: _ => println(s"Something starts with int:$a, string:$b")
    case _                            => println("something Else")
  }

}
