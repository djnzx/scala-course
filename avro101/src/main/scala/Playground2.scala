import org.apache.avro.generic.{GenericData, GenericRecord}
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

  val p: GenericData.Record = user6

  val f1: Option[String] = p.go[String]("name")
  val f2: Option[GenericRecord] = p.go[GenericRecord]("address")
  val f3: Option[String] = p.go[String]("address.street")
  val f4: Option[Long] = p.go[Long]("address.house")
  val f5: Option[Long] = p.go[Long]("a")
  val f6: Option[Double] = p.go[Double]("address.street.x")

  println(p)
  println(p.getSchema)
  println(f1)
  println(f2)
  println(f3)
  println(f4)
  println(f5)
  println(f6)
}
