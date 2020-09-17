package hackerrank.d200917

object SherlockAndAnagramsPlain extends App {

  def toMap(s: String) = s.toSeq.groupMapReduce(identity)(_ => 1)(_+_)
  def isAnagram(s1: String, s2: String) = toMap(s1) == toMap(s2)
  def allCombinations(slen: Int) =
    (1 until slen).flatMap { len =>
      (0 to slen-1-len).flatMap { idx1 =>
        (idx1 + 1 to slen-len).map { idx2 =>
          (len, idx1, idx2)
        }
      }
    }
    
  def allSubs(s: String, data: Seq[(Int, Int, Int)]) =
    data.map { case (len, idx1, idx2) =>
    (
      s.substring(idx1, idx1 + len),
      s.substring(idx2, idx2 + len)
    )
  }

  def sherlockAndAnagrams(s: String) =
    allSubs(s, allCombinations(s.length))
      .count { case (s1, s2) => 
        isAnagram(s1, s2)
      }
}
