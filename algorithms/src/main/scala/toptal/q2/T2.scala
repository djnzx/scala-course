package toptal.q2

/**
  * minimal length
  * of subarray, containing
  * all unique elements
  *
  * [7, 3, 7, 3, 1, 3, 4, 1] -> 5: [7, 3, 1, 3, 4]
  * [2, 1, 1, 3, 2, 1, 1, 3] -> 3: [3, 2, 1]
  * [7, 5, 2, 7, 2, 7, 4, 7] -> 6: [5, 2, 7, 2, 7, 4]
  *
  * solutions: 
  * - [[twopointers.TwoPointersIterative]]
  * - [[twopointers.TwoPointersRecursive]]
  * - [[twopointers.TwoPointersRecursiveImmutable]]
  * test: [[twopointers.TwoPointersSpec]]
  */
object T2