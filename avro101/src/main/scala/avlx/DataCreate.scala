package avlx

import org.apache.avro.generic.GenericData

object DataCreate {

  /** record create */
  val user1: GenericData.Record = new GenericData.Record(SchemaCreate.schema1a)
  user1.put("name", "Jim")
  user1.put("age", 33)

  val user2: GenericData.Record = new GenericData.Record(SchemaCreate.schema1a)
  user2.put("name", "Jack")
  user2.put("age", 44)

  val user3: GenericData.Record = new GenericData.Record(SchemaCreate.schema1a)
  user3.put("name", "Alex")

  val user4: GenericData.Record = new GenericData.Record(SchemaCreate.schema1a)
  user4.put("age", 55)

  val user5: GenericData.Record = new GenericData.Record(SchemaCreate.schema1a)

  /** generic data fails only on absent attribute names */
  //    user1.put("whatever", new Object)

}
