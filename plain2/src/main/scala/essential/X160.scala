package essential

import scala.annotation.unused
import scala.collection.immutable.Queue

object X160 extends App {
  /**
    * Sequence: defined and stable order
    * Set[T]
    * Map[K,V] = Set[MapEntry[K,V]]
    *
    */
  class ABC {
    def apply(@unused num: Int): ABC = {
//      println(num)
      new ABC()
    }
  }
  val abc = new ABC;
  val abc2 = abc(1) // apply method call. no magic

  val s1: Seq[Int] = Seq(1,2,3) // actually produces List[T]
  s1.head
  s1.headOption
  s1.tail
  s1.tails
  s1.length
  s1.last
  s1.contains(1)
  val itm1: Option[Int] = s1.find(_ % 2 == 0)
  s1.sortWith(_ > _) // sort by boolean. true if in order
  val s2 = s1 :+ 4 // value to list from the right side
  val s3 = 0 +: s2 // value to list from the left side
  val s4 = Seq(1,2,3) ++ Seq(4, 5, 6)

  /**
    * Seq by default is a List
    * List by default is a LinkedList
    * by default all is immutable
    *
    * :: and ::: List only operations
    */
  val list1: List[Int] = List(1,2,3)
  val list2: List[Int] = 1 :: 2 :: 3 :: Nil
  val list3: List[Any] = 1 :: "abc" :: true :: Nil
  val list4: List[Int] = List(1,2,3) ::: List(4,5,6)

  val v1: Vector[Int] = Vector(1,2,3,4,5)
  val q1: Queue[Int] = Queue(1, 2, 3, 5, 6, 7)
  val q2 = q1.prepended(44)               // add ONE  item  to the front of the queue
  val q3 = q1.prependedAll(Seq(44,55,66)) // add MANY items to the front of the queue

  val q51 = q1.appended(77)               // add ONE  item  to the end of the queue
  val q52 = q1.appendedAll(Seq(77,88))    // add MANY items to the end of the queue
  val q53 = q1.enqueue(99)                // -------- "" -------
  val q54 = q1.enqueueAll(List(98,99))    // -------- "" -------

  val (head, tail) = q1.dequeue           // take one from the queue (1,Queue(2, 3, 5, 6, 7)) or 'NoSuchElementException'
  q1.dequeueOption                        // the same ^ but to option
  val q60 = q1.drop(1)                    // DROP numbers of elements from the FRONT of the queue
  val q61 = q1.dropRight(1)               // DROP numbers of elements from the END of the queue
  val q62 = q1.head                       // take the head or Exception
  val q63 = q1.headOption                 // take the head to Option w/o Exception
  val q64 = q1.last                       // take the last element or Exception
  val q65 = q1.lastOption                 // take the last element to Option w/o Exception
  val q66 = q1.tail                       // tail of the queue
  List(1,2,3).head                        // take
  List(1,2,3).take(1)                     // take and remove

  val l1: List[Int] = List(1,2)
  // val l2: List[Int] = l1 :+ "abc" // error in compile time
  val l3: List[Any] = l1 :+ "abc"

  val hh = "abc".toSeq.permutations.toList
  val pm1 = Seq("a", "wet", "dog").map(_.permutations.toList)
  val pm2 = Seq("a", "wet", "dog").flatMap(_.permutations.toList)
  println(pm1)
  println(pm2)
}
