package avlx

import java.io.File
import tools.Tools.readFrom

object DataRead {

  def delimiter() = println("-" * 50)

  def main(as: Array[String]): Unit = {
    val FILE = new File("persons.avro")

    /** read without schema enforced */
    readFrom(FILE)
      .foreach { x => println(s"schema: ${x.getSchema}, data: $x") }

    delimiter()

    /** read WITH schema enforced */
    readFrom(FILE, SchemaCreate.schema1a)
      .foreach { x => println(s"schema: ${x.getSchema}, data: $x") }

    delimiter()

    /** read WITH schema enforced, should fail, schema specified has one field isn't exist on th data saved */
    readFrom(FILE, SchemaCreate.schema1c)
      .foreach { x => println(s"schema: ${x.getSchema}, data: $x") }
  }

}
