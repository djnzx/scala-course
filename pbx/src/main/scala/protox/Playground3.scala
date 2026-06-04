package protox

import pbx.car3.Car3
import pbx.car3no.Car3no

object Playground3 extends App {

  new Car3(1, Some("Jeep"))
  val c3 = new Car3no()
  pprint.pprintln(c3.id)
  pprint.pprintln(c3.details)

}
