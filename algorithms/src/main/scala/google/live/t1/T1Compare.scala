package google.live.t1

import T1Domain._

import scala.collection.mutable

/**
  * compare all to all: O(N^2^)
  */
object T1Compare {
  /**
    * we collect all rectangles which are bigger
    * to map: {{{Map[R, Set[R]]}}}
    *
    * a>b, a>c, a>d, b>e
    *
    * becomes:
    *
    * a->(b,c,d) b->(e)
    *
    */
  def compareAllToMap(rs: IndexedSeq[R]) = {
    val all = rs.indices
    val outcome = mutable.Map.empty[R, Set[R]]

    all.foreach { i =>
      all.foreach { j =>
          if (gt(rs(i), rs(j))) {
            outcome.updateWith(rs(i)) {
              case None     => Some(Set(rs(j)))
              case Some(ss) => Some(ss + rs(j))
            }
          }
        }
    }

    outcome
  }

  /**
    * collect result to {{{Array[List[Int]]}}}
    */
  def compareAllToArray(rs: Seq[R]) = {
    val outcome = Array.fill[List[Int]](rs.length)(Nil)
    val all = rs.indices
    
    all.foreach { i =>
      all
        .filter(j => gt(rs(i), rs(j)))
        .foreach(j => outcome(i) = j :: outcome(i))
    }
    
    outcome
  }
  
  
}
