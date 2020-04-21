package hackerrank

/**
  * https://www.hackerrank.com/challenges/big-sorting/problem
  */
object BigSorting extends App {

  def bigSorting1(unsorted: Array[String]): Array[String] =
    unsorted.sortWith { (s1, s2) =>
      if      (s1.length < s2.length) true
      else if (s1.length > s2.length) false
      else (s1 zip s2).foldLeft((true, 0)) { (acc, t) => acc match {
          case (true, _) =>
            if      (t._1 < t._2) (false, -1)
            else if (t._1 > t._2) (false,  1)
            else                  (true,   0)
          case _ => acc
        }}
          ._2 < 0
    }

  def bigSorting2(unsorted: Array[String]): Array[String] =
    unsorted.sortWith { (s1, s2) =>
      if      (s1.length < s2.length) true
      else if (s1.length > s2.length) false
      else s1.compareTo(s2)<0
    }

}
