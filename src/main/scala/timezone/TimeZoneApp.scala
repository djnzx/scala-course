package timezone

import java.time.ZonedDateTime
import java.util.TimeZone

/**
  * https://stackoverflow.com/questions/2493749/how-to-set-a-jvm-timezone-properly
  * https://www.timeanddate.com/time/gmt-utc-time.html
  */
object TimeZoneApp extends App {
  /**
    * It will take from the computer properties
    * 2020-05-14T15:35:25.010+03:00[Europe/Kiev]
    */
  println(ZonedDateTime.now())

  /**
    * sets the Zone
    * for further usage in your App
    */
  TimeZone.setDefault(TimeZone.getTimeZone("GMT"))
  println(ZonedDateTime.now())
}
