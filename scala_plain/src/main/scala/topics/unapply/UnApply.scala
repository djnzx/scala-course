package topics.unapply

object UnApply extends App {
  
  class Person(id: Int, name: String)
  
  object PA {
    def apply(id: Int, name: String) = new Person(id, name)
  }
  
  object PX {
    def unapply(arg: Person): Option[(Int, String)] = Some((id, name))
  }
  
  val p1: Person = PA(33, "Jim")
  
  val PX(id, name) = p1

  import java.time.{LocalDate, LocalDateTime, LocalTime}

  object Date {
    def unapply(d: LocalDate): Option[(Int, Int, Int)] = Some((d.getYear, d.getMonthValue, d.getDayOfMonth))
  }

  object Time {
    def unapply(t: LocalTime): Option[(Int, Int, Int)] = Some((t.getHour, t.getMinute, t.getSecond))
  }

  object DateTime {
    def unapply(dt: LocalDateTime): Option[(LocalDate, LocalTime)] = Some((dt.toLocalDate, dt.toLocalTime))
  }
  
  object AM {
    def unapply(t: LocalTime): Option[(Int, Int, Int)] =
      t match {
        case Time(h, m, s) if h < 12 => Some((h, m, s))
        case _ => None
      }
  }

  object PM {
    def unapply(t: LocalTime): Option[(Int, Int, Int)] =
      t match {
        case Time(12, m, s) => Some(12, m, s)
        case Time(h, m, s) if h > 12 => Some(h - 12, m, s)
        case _ => None
      }
  }
  
  object DateTimeSeq {
    def unapplySeq(dt: LocalDateTime): Some[Seq[Int]] =
      Some(Seq(
        dt.getYear, dt.getMonthValue, dt.getDayOfMonth,
        dt.getHour, dt.getMinute, dt.getSecond))
  }
  
  object EvenInt {
    def unapply(o: Any): Option[Int] = o match {
      case i: Int if i % 2 == 0 => Some(i)
      case _ => None  
    }
  }

  /**
    * collect expects Partial Function => so we can use pattern matching
    */
  (1 to 5)
    .collect {
      case EvenInt(x) => x
    }
    .foreach { x => println(x) }

  /**
    * filter expects predicate, so we need to make it full
    */
  (1 to 5)
    .filter {
      case EvenInt(_) => true
      case _ => false
    }
    .foreach { x => println(x) }


  val t = LocalTime.now match {
    case t @ AM(h, m, _) => f"$h%2d:$m%02d AM ($t precisely)"
    case t @ PM(h, m, _) => f"$h%2d:$m%02d PM ($t precisely)"
  }
  
  println(t)

}
