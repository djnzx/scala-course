package c06x02_GenericMergeSort

import MergeSort._

object MergeSortTest extends App {
  val input = Vector("banana", "mandarin", "avocado", "apple", "mango", "cherry", "mangosteen")

  assert(
    pprint.log(mergeSort(input)) ==
      Vector("apple", "avocado", "banana", "cherry", "mandarin", "mango", "mangosteen")
  )

}