package google

import scala.collection.mutable
import scala.util.Random

object Task1NestedRectanglesCountOnly extends App{
  import Task1NestedRectangles._
  
  def count(rs: IndexedSeq[R]) = {
    val dp = mutable.Map.empty[R, Set[R]]
    
    rs.indices.foreach { i =>
      rs.indices
        .filter(j => j != i)
        .foreach { j => // fit to all
          if (gt(rs(i), rs(j))) {
//            println(s"${rs(i)} > ${rs(j)}")
            dp.updateWith(rs(i)) {
              case None     => Some(Set(rs(j)))
              case Some(ss) => Some(ss + rs(j))
            }
          }
        }
    }
    
    dp
  }
  
  val rr = IndexedSeq(
    (100,100),
    (20,90),
    (30,80),
    (40,70),
    (50,60),
    (60,50),
    (70,40),
    (80,30),
    (90,20),
  )

  def next() = Random.nextInt(1000)
  def randomT() = (next(), next())
  val rects = LazyList.fill(100)(randomT()).toIndexedSeq

  val x = count(rects)
//    .map { case (_,v) => v.size }
//    .sum
  println(x.size)
  println(x.map { case (_,v) => v.size }.sum)
  
  def chains(m: mutable.Map[R, Set[R]]) = {
    def chainsFor(r: R): List[List[R]] = m.contains(r) match {
      case false => List(Nil)
      case true => m(r).flatMap { ch => chainsFor(ch).map(r :: _)}.toList
    }
    m.keys.flatMap(p => chainsFor(p).map(p :: _))
  }
  
  def lengths(m: mutable.Map[R, Set[R]]) = {
    def maxLenFor(r: R): Int = m.get(r) match {
      case None    => 0
      case Some(x) => x.map { x => 1 + maxLenFor(x) }.max
    }
    m.keys.map(x => 1 + maxLenFor(x)).max
  }
  
//  chains(count(rectangles.toIndexedSeq))
//  val z = chains(x)
//    .maxBy(_.length).length
//    
//  pprint.log(z)
  pprint.log(lengths(count(rectangles.toIndexedSeq)))
  def time() = System.currentTimeMillis()
  val start = time()
  pprint.log(lengths(x))
  val delta = time() - start
  println(s"time:${delta}ms")

}
