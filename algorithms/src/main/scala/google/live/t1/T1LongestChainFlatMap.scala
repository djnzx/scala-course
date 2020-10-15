package google.live.t1

import scala.collection.mutable

object T1LongestChainFlatMap {
  import T1Domain._
  
  def chainsFromMap(m: mutable.Map[R, Set[R]]) = {
    def chainsFor(r: R): List[List[R]] = m.contains(r) match {
      case false => List(Nil)
      case true => m(r).flatMap { ch => chainsFor(ch).map(r :: _) }.toList
    }

    m.keys.flatMap(p => chainsFor(p).map(p :: _))
  }

  def maxLenFromMap(m: mutable.Map[R, Set[R]]) = {
    def maxLenFor(r: R): Int = m.get(r) match {
      case None => 0
      case Some(x) => x.map { x => 1 + maxLenFor(x) }.max
    }

    m.keys.map(x => 1 + maxLenFor(x)).max
  }

  def maxLenFromArray(m: Array[List[Int]]) = {
    def maxLenFor(r: Int): Int = m(r) match {
      case Nil => 1
      case ls  => 1 + ls.map(maxLenFor).max
    }
    m.indices.map(maxLenFor).max
  }

}
