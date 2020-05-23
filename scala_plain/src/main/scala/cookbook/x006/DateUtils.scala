package cookbook.x006

import java.text.SimpleDateFormat
import java.util.Calendar

object DateUtils {
  def getCurrentDate = getCurrentDateTime("EEEE, MMMM, d")

  def getCurrentTime = getCurrentDateTime("K:m aa")

  private def getCurrentDateTime(dtf: String) = {
    val df = new SimpleDateFormat(dtf)
    val cal = Calendar.getInstance()
    df.format(cal.getTime)
  }
}
