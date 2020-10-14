package toptal.q2.twopointers

/**
  * Two Pointers
  * minimal length of subarray,
  * containing K unique elements
  *
  * [7, 3, 7, 3, 1, 3, 4, 1] -> 5: [7, 3, 1, 3, 4]
  * [2, 1, 1, 3, 2, 1, 1, 3] -> 3: [3, 2, 1]
  * [7, 5, 2, 7, 2, 7, 4, 7] -> 6: [5, 2, 7, 2, 7, 4]
  */
trait TwoPointers {
  /**
    * 
    * @param a - Indexed Sequence of elements
    * @param k - number of unique elements should be in array 
    * @tparam A
    * @return
    *   0   - seq. is empty
    *   -1  - there is no K distinct elements in array
    *   > 0 - shortest length of subarray
    */
  def find[A](a: IndexedSeq[A], k: Int): Int
}
