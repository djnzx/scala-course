package circe101.c5_adt

object Model {

  sealed trait Event
  object Event {
    case class Foo(i: Int)    extends Event
    case class Bar(s: String) extends Event
  }

}
