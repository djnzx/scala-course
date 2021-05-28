package topics

object ValueTypesBehavior extends App {

  /**
    * having private makes unapply stop working since 2.13.4+
    * compiler will throw an long stacktrace
    */
//  case class CarId(private val src: Int) extends AnyVal
  case class CarId(src: Int) extends AnyVal

  val cars: Seq[CarId] = Seq(CarId(1), CarId(2))

  cars foreach {
    case CarId(id) => println(id)
    case _ => println("non CarID instance in the sequence!")
  }

}
