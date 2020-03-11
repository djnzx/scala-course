package cats

import java.time.{LocalDate, LocalTime, ZoneId}
import java.time.format.DateTimeFormatter

import cats.syntax.all._

object C023Show extends App {

  val ld = LocalDate.now()
  val lt = LocalTime.now()                            // current time according to system settings
  val lt2 = LocalTime.now(ZoneId.of("GMT+1")) // current time in requested zone, based on system settings
  val fmtd = DateTimeFormatter.ofPattern(">dd.MM.yyyy<")
  val fmtt = DateTimeFormatter.ofPattern(">HH:mm:ss<")

  implicit val showDate: Show[LocalDate] = (t: LocalDate) => t.format(fmtd)
  implicit val showTime: Show[LocalTime] = (t: LocalTime) => t.format(fmtt)

  val s1: String = ld.show
  val s2: String = lt.show
  println(s1)
  println(s2)

}
