package x081collections

import scala.collection.immutable.{HashMap, ListMap, VectorMap}

object Collections002Immutable extends App {

  // declaration only
  val fibs: LazyList[BigInt] = BigInt(1) #:: BigInt(1) #:: fibs.zip(fibs.tail).map( n => n._1 + n._2 )
  // evaluation on access and memoization!
  fibs.take(6).foreach(println)

  /**
    * map using both a Vector of keys and a HashMap.
    * It provides an iterator that returns all the entries in their insertion order
    */
  val m1 = VectorMap()

  val m2 = HashMap()
  val m3 = ListMap()
}
