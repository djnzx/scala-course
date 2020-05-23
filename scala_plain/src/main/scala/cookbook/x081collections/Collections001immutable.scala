package cookbook.x081collections

import java.awt.Color

import scala.collection.{BitSet, LinearSeq}
import scala.collection.immutable.{HashSet, ListSet, Queue, SortedSet, TreeSet}

object Collections001immutable extends App {
  /**
    * scala.collection
    * scala.collection.immutable
    * scala.collection.mutable
    *
    * By default, Scala always picks immutable collections
    */

  /**
    * Iterable: Seq, Set, Map
    * Seq: IndexedSeq, LinearSeq
    *
    * IndexedSeq: Vector, Range, String (index based)
    * LinearSeq: any List (fast append)
    * Set -> SortedSet
    * Map -> SortedMap
    */
  val i1 = Iterable(1,2,3)  // Seq
  val i2 = Seq(1,2,3)       // LinearSeq
  val i3 = LinearSeq(1,2,3) // List
  val i4 = List(1,2,3)      // List


  val i5 = Queue(1,2,3)
  val i6 = i5.appended(4)
  i5.dequeue

  val i51 = IndexedSeq(1,2,3)

  val m1 = Map("x" -> 24, "y" -> 25, "z" -> 26)

  val s1 = HashSet(1,2,3)
  val s2 = ListSet(1,2,3) // based on list
  val s3 = Set(Color.red, Color.green, Color.blue) // HashSet

  val s4 = SortedSet("hello", "world") // TreeSet
  val s5 = TreeSet(5,1,10,5) // 1,5,10
  val s6 = BitSet(1,2,3,3) // efficient non-negative integers, size determined by max value

}
