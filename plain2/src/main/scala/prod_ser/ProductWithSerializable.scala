package prod_ser

object ProductWithSerializable extends App {

  sealed trait Base
  final case object Foo extends Base
  final case object Bar extends Base
  /**
    * the type of list is:
    * List[Product with Base with Serializable]
    */
  val list = List(Foo, Bar)

  
  sealed trait Color extends Product with Serializable
  final case object Red extends Color
  final case object Yellow extends Color
  final case object Green extends Color
  /**
    * the type of list is:
    * List[Color]
    */
  val colors = List(Red, Yellow, Green)
  
}
