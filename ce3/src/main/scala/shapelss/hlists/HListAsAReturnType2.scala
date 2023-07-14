package shapelss.hlists

import shapeless._

object HListAsAReturnType2 extends App {

  // somehow provide the builder of type A
//  def doIt[A <: HList, H, T <: HList](x: Int)(implicit ev: A <:< H :: T): A = {
//
//
//    ???
//  }
//    x match {
//    case 1 => 1 :: HNil
//    case 2 => 1 :: "two" :: HNil
//    case 3 => 1 :: "two" :: '3' :: HNil
//    case 4 => 1 :: "two" :: '3' :: 3.14 :: HNil
//    case _ => HNil.asInstanceOf[HList]
//  }

//  doIt(2) match {
//    case a :: b :: _ => println(s"Something starts with int:$a, string:$b")
//    case _           => println("something Else")
//  }

}
