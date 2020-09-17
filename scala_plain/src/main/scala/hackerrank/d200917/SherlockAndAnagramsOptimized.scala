package hackerrank.d200917

object SherlockAndAnagramsOptimized extends App {

  def toMap(s: String) = s.toSeq.groupMapReduce(identity)(_ => 1)(_+_)
  def isAnagram(s1: String, s2: String) = toMap(s1) == toMap(s2)

  def sherlockAndAnagrams(s: String) =
    (1 until s.length).flatMap { len =>
      (0 to s.length-1-len).flatMap { idx1 =>
        (idx1 + 1 to s.length-len).map { idx2 =>
          (len, idx1, idx2)
        }
      }
    }
      .foldLeft(0) { case (acc, (len, idx1, idx2)) =>
        acc + (if (isAnagram(s.substring(idx1, idx1 + len), s.substring(idx2, idx2 + len))) 1 else 0)
      }
}
