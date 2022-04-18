package shapelss.book

import shapeless.::
import shapeless.HList
import shapeless.HNil
import shapeless.the
import shapeless.ops.hlist.Last
import shapeless.ops.hlist.Last.Aux

object C04 extends App {
  val last1: Aux[String :: Int :: HNil, Int] = Last[String :: Int :: HNil]
  //                        ^            ^
  val last2: Aux[Int :: String :: HNil, String] = Last[Int :: String :: HNil]
  //                       ^              ^
  val l1: Int = last1("A" :: 1 :: HNil)
  val l2: String = last2(2 :: "B" :: HNil)
}
