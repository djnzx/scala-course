package __udemy.scala_beginners.lectures.part3fp._map

/**
  * Created by Daniel.
  */
object TuplesAndMaps extends App {

  // Maps - keys -> values
  val aMap: Map[String, Int] = Map()

  val phonebook = Map(("Jim", 555), "Daniel" -> 789, ("JIM", 9000)).withDefaultValue(-1)
  // a -> b is sugar for (a, b)
  println(phonebook)

  // map ops
  println(phonebook.contains("Jim")) // true
  println(phonebook("Mary")) // -1

  // add a pairing
  val newPairing = "Mary" -> 678
  val newPhonebook = phonebook + newPairing
  println(newPhonebook) // Map(Jim -> 555, Daniel -> 789, JIM -> 9000, Mary -> 678)

  // map, flatMap, filter
  println(phonebook.map(pair => pair._1.toLowerCase -> pair._2)) // we lose Jim -> 555

  // filterKeys
  println(phonebook.filterKeys(x => x.startsWith("J")))
  // mapValues
  println(phonebook.mapValues(number => "0245-" + number))

  // conversions to other collections
  println(phonebook.toList)
  println(List(("Daniel", 555)).toMap)
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println("-----")
  println(names.groupBy(name => name.charAt(0)))
  println("-----")
}
