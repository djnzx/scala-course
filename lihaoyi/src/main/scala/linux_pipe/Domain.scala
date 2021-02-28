package linux_pipe

object Domain {

  import upickle.default.{ReadWriter => RW, macroRW}

  case class Item(event_type: String, data: String, timestamp: Long)

  object Item {
    implicit val rw: RW[Item] = macroRW
  }

  case class Frame(started: Long, data: Map[String, Map[String, Int]])

  object Frame {
    def empty(timestamp: Long) = Frame(timestamp, Map.empty)
  }

}
