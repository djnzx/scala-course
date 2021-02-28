package linux_pipe

object Domain {

  import upickle.default.{ReadWriter => RW, macroRW}

  /** event representation */
  case class Item(event_type: String, data: String, timestamp: Long)

  object Item {
    /** JSON reader-writer */
    implicit val rw: RW[Item] = macroRW
  }

  /** counter implementation */
  case class Counter(data: Map[String, Map[String, Int]]) {
    def count(item: Item): Counter = Counter(data.updatedWith(item.event_type) {
      case None    => Some(Map(item.data -> 1))
      case Some(m) => Some(m.updatedWith(item.data) {
        case None    => Some(1)
        case Some(n) => Some(n + 1)
      })
    })
  }
  
  object Counter {
    def empty = Counter(Map.empty[String, Map[String, Int]])
    def startNew(item: Item) = Counter.empty.count(item)
    /** JSON reader-writer */
    implicit val rw: RW[Counter] = macroRW
  }
  
  /** frame representation */
  case class Frame(started: Long, cnt: Counter, lifeSpan: Long) {
    
    def isFrameExpired(at: Long) = at > started + lifeSpan
    
    def combine(item: Item): Frame = isFrameExpired(item.timestamp) match {
      /** start new one */
      case true => copy(started = item.timestamp, Counter.startNew(item))
      /** keep collecting */
      case false => copy(cnt = cnt.count(item))
    }

  }

  object Frame {
    def next(timestamp: Long, length: Long) = Frame(timestamp, Counter.empty, length)
  }

}
