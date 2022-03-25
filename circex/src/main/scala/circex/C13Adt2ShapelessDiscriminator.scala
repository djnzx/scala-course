package circex

import io.circe.generic.extras.auto._
import io.circe.generic.extras.Configuration
import io.circe.jawn.decode
import io.circe.syntax._

object C13Adt2ShapelessDiscriminator extends App {
  import ModelAdt._

  implicit val c: Configuration = Configuration.default.withDiscriminator("type")

  println(Foo(33).asJson.noSpaces) // {"i":33}

  val raw =
    """
      |{
      |"i":33,
      |"type":"Foo"
      |}
      |""".stripMargin

  pprint.pprintln(decode[Event](raw)) // Right(value = Foo(i = 33))

}
