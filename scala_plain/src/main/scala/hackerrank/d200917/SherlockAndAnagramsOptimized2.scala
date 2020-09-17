package hackerrank.d200917

object SherlockAndAnagramsOptimized2 extends App {

  def toMap(s: String) = s.toSeq.groupMapReduce(identity)(_ => 1)(_+_)
  def isAnagram(s1: String, s2: String) = toMap(s1) == toMap(s2)

  def countWithLen(s: String, len: Int) =
    (0 to s.length-1-len).flatMap { idx1 =>
      (idx1 + 1 to s.length-len).map { idx2 =>
        (idx1, idx2)
      }
    }
      .foldLeft(0) { case (cnt, (idx1, idx2)) =>
        
        val s1 = s.substring(idx1, idx1 + len)
        val s2 = s.substring(idx2, idx2 + len)
        cnt + (if (isAnagram(s1, s2)) { println(s"$s1:$s2"); 1; } else 0)
      }
  
  
  def sherlockAndAnagrams(s: String) =
    (1 until s.length).foldLeft((true, 0)) { case ((cont, cnt), len) =>
//      if (!cont) (false, cnt)
//      else 
        countWithLen(s, len) match {
//        case 0    => (false, cnt)
        case cl@_ => (true, cnt + cl)
      }
    }._2
  
}
