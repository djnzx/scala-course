package x00lessons.tips12

import scala.util.Random

/**
  * andThen:
  * https://alvinalexander.com/source-code/fun-with-scala-functions-andthen-compose
  */

object Tip04FoldForever extends App {
  // fold forever!
  def loadConfig(): Either[Throwable, String] = Random.nextInt(2) match {
    case 1 => Right("MyData!")
    case _ => Left(new IllegalArgumentException("Wrong!!!"))
  }

  loadConfig() match {
    case Left(_)      => println("> no data < ")    // use default values
    case Right(value) => println(s"using: $value")  // use my value
  }

  val s: String = loadConfig().fold(_ => ">> default val <<", x => x)

  val inc1 = (i: Int) => i + 1
  val mul2 = (i: Int) => i * 2

  /**
    * old-fashioned approach (Java)
    * Option.map(profile => profile.lookupCountry)
    * .getOrElse(getCountryByIp)
    */

  val opt: Option[Int] =
    Option(1)
//    None
  val r: Int = opt
    // full syntax
//    .map(_ + 1).getOrElse(42)
    // fold syntax, composition
      .fold(42)(inc1 andThen mul2)
  println(r)


}
