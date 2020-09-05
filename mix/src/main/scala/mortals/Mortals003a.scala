package mortals

import scalaz._
import Scalaz._
import scala.concurrent.duration._

object Mortals003a {
  val s5: FiniteDuration = 5.seconds
  
  
  final case class Epoch(millis: Long) extends AnyVal {
    def +(d: FiniteDuration): Epoch = Epoch(millis + d.toMillis) 
    def -(e: Epoch): FiniteDuration = (millis - e.millis).millis
  }
}
