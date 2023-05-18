package shapelss.hlists

import shapeless._

/** https://akmetiuk.com/posts/2016-09-30-dissecting-shapeless-hlists.html */
object HListsApp extends App {

  val hList: Int :: Double :: String :: Boolean :: HNil = 1 :: 3.14 :: "Jim" :: true :: HNil
  val head: Int = hList.head
  val tail: Double :: String :: Boolean :: HNil = hList.tail

  pprint.pprintln(implicitly[sourcecode.FileName]) // "HListsApp.scala"
  pprint.pprintln(implicitly[sourcecode.Name]) // HListsApp
  pprint.pprintln(implicitly[sourcecode.Line]) // 14
  pprint.pprintln(hList)

  def printR(hl: HList): Unit = hl match {
    case _: HNil => println("HNil")
    case h :: t  => println(h); printR(t)
  }

  printR(hList)
}
