package avlx

import org.apache.avro.generic.GenericData
import tools.Tools.{validateRecord, writeTo}

import java.io.File

object DataCreate {

  def main(as: Array[String]): Unit = {

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

    println(user1) // {"name": "Jim", "age": 33}
    println(validateRecord(user1))

    println(user2) // {"name": "Jack", "age": 44}
    println(validateRecord(user2))

    println(user3) // {"name": "Alex", "age": null}
    println(validateRecord(user3))

    println(user4) // {"name": null, "age": 55}
    println(validateRecord(user4)) // false

    println(user5) // {"name": null, "age": null}
    println(validateRecord(user5)) // false

//    val FILE = new File("persons.avro")
//    writeTo(FILE, SchemaCreate.schema1a, user1, user2, user3, user4)
  }

}
