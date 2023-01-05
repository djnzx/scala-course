package avlx

import tools.Console.delimiter
import tools.Tools.readFrom

import java.io.File

object DataRead {

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
