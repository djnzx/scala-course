package avlx

import avlx.DataCreate._
import tools.Tools.validateRecord

object DataValidate {

  def main(as: Array[String]): Unit = {
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
  }

}
