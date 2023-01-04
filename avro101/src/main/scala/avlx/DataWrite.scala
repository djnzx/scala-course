package avlx

import tools.Tools.writeTo

import java.io.File

object DataWrite {

  import DataCreate._

  def main(as: Array[String]): Unit = {
    val FILE = new File("persons.avro")
    writeTo(FILE, SchemaCreate.schema1a, user1, user2, user3)
  }

}
