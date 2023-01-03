package avlx

import org.apache.avro.Schema
import org.apache.avro.SchemaBuilder
import tools.Utils.contentsFromResources

object SchemaCreate {

  /** create schema programatically */
  // format: off
  val schema1a = SchemaBuilder.record("person").fields()
    .name("name").`type`.stringType.noDefault
    .name("age").`type`.nullable.intType.noDefault
    .endRecord
  // format: on
  /** create the same schema by reading from file */
  val contents = contentsFromResources("0/schema1.json")
  val schema1b = new Schema.Parser().parse(contents)

  def main(as: Array[String]) = {
    println(schema1a)
    println(schema1b)
  }

}
