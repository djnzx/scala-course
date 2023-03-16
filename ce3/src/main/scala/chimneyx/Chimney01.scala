package chimneyx

import java.time.ZonedDateTime
import scala.util.Random

object Chimney01 extends App {

  case class MakeCoffee(id: Int, kind: String, addict: String)
  case class CoffeeMade(id: Int, kind: String, forAddict: String, at: ZonedDateTime)

  // typical
  val command = MakeCoffee(id = Random.nextInt, kind = "Espresso", addict = "Piotr")
  // a lot of boiler plate
  val event = CoffeeMade(id = command.id, kind = command.kind, forAddict = command.addict, at = ZonedDateTime.now)

  import io.scalaland.chimney.dsl._
  val event2 = command
    .into[CoffeeMade]
    .withFieldComputed(_.at, _ => ZonedDateTime.now)
    .withFieldRenamed(_.addict, _.forAddict)
    .transform

  pprint.pprintln(event2)

}
