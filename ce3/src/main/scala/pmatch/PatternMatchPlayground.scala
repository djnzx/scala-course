package pmatch

object PatternMatchPlayground extends App {

  case class Id(id: Int)

  val myValue = 13
  val id1 = Id(13)
  val id2 = Id(1)

  Seq(
    id1,
    id2,
  ).foreach {
    case Id(`myValue`) => pprint.pprintln(13)
    case myId => pprint.pprintln(myId)
  }


}
