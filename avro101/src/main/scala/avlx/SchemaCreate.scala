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
  /** create the same schema by reading from file */
  val contents = contentsFromResources("0/schema1.json")
  val schema1b = new Schema.Parser().parse(contents)

  val schema1c = SchemaBuilder.record("person").fields()
    .name("name").`type`.stringType.noDefault
    .name("age").`type`.nullable.intType.noDefault
    .name("skill").`type`.stringType.noDefault
    .endRecord

  val addressSchema = SchemaBuilder.record("address").fields()
    .name("street").`type`.stringType.noDefault
    .name("house").`type`.longType().noDefault
    .endRecord()

  val schema1d = SchemaBuilder.record("person2").fields()
      .name("name").`type`.stringType.noDefault
      .name("age").`type`.nullable.intType.noDefault
      .name("address").`type`(addressSchema).noDefault
      .endRecord
  // format: on

  def main(as: Array[String]) = {
    println(schema1a)
    println(schema1b)
    println(schema1d)
  }

}
