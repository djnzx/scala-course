package hlist

import shapeless.HNil
import shapeless.::

object HListApp extends App {
  type HList0 = HNil
  type HList1 = Int :: HNil
  type HList2 = Int :: String :: Boolean :: HNil

  val emptyHList1 = HNil
  val emptyHList2: HList0 = HNil
  val emptyHList3: HNil = HNil

  val shortHList1 = 123 :: HNil
  val shortHList2: HList1 = 123 :: HNil
  val shortHList3: Int :: HNil = 123 :: HNil

  val longerHList1
  = 123 :: "abc" :: true :: HNil
  val longerHList2: HList2
  = 123 :: "abc" :: true :: HNil
  val longerHList3: Int :: String :: Boolean :: HNil
  = 123 :: "abc" :: true :: HNil

  println(shortHList1)      // 123 :: HNil
  println(shortHList1.head) // 123
  println(shortHList1.tail) // HNil
}
