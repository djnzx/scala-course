package linux_pipe

object Domain {

  import upickle.default.{ReadWriter => RW, macroRW}

  case class Item(event_type: String, data: String, timestamp: Long)

  object Item {
    implicit val rw: RW[Item] = macroRW
  }

  type Counter = Map[String, Map[String, Int]]
  val emptyData = Map.empty[String, Map[String, Int]]
  
  case class Frame(started: Long, data: Counter, longevity: Int) {
    
    def newIfExpired(item: Item): (Counter, Long) = item.timestamp < started + longevity match {
      case true => (data, started)
      case _    => (emptyData, item.timestamp)
    }

    def combine(item: Item): Frame =
      newIfExpired(item) match {
        case (data, newStarted) =>
          val newData = data.updatedWith(item.event_type) {
            case None       => Some(Map(item.data -> 1))
            case Some(freq) => Some(freq.updatedWith(item.data) {
              case None    => Some(1)
              case Some(n) => Some(n + 1)
            })

          }
          copy(data = newData, started = newStarted)
      }

  }

  object Frame {
    def empty(timestamp: Long, length: Int) = Frame(timestamp, Map.empty, length)
  }

}
