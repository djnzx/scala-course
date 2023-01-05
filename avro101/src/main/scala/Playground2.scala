import org.apache.avro.generic.GenericRecord
import pprint.{pprintln => println}
import tools.Console.delimiter

object Playground2 extends App {

  import avlx.DataCreate._
  import genrec.syntax._

  val x1: Option[String] = user1.go[String]("name")
  val x2: Option[Int] = user1.go[Int]("age")
  println(x1)
  println(x2)

  delimiter()

  println(user6)
  println(user6.go[String]("name")) // Some("Jackery")
  println(user6.go[GenericRecord]("address")) // Some({"street": "Broadway", "house": 123})
  println(user6.go[String]("address.street")) // Some("Broadway")
  println(user6.go[Long]("address.house")) // Some(123L)
}
