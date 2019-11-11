package x060essential

object X160 extends App {
  /**
    * Sequence: defined and stable order
    * Set[T]
    * Map[K,V] = Set[MapEntry[K,V]]
    *
    */
  class ABC {
    def apply(num: Int): ABC = {
      println(num)
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
    */
  val list1 = List(1,2,3)
  val list2: Seq[Int] = 1 :: 2 :: 3 :: Nil
  val list3: Seq[Any] = 1 :: "abc" :: true :: Nil





}
