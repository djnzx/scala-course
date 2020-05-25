package lihaoyi.c06

object MergeSortApp {

  def mergeSort[A: Ordering](items: IndexedSeq[A]): IndexedSeq[A] =
    if (items.length <= 1) items
    else {
      val (left, right) = items.splitAt(items.length / 2)
      val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))

      var (leftIdx, rightIdx) = (0, 0)
      val output = IndexedSeq.newBuilder[A]

      while (leftIdx < sortedLeft.length || rightIdx < sortedRight.length) {
        val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match {
          case (true, false) => true
          case (false, true) => false
          case (true, true) => implicitly[Ordering[A]].lt(sortedLeft(leftIdx), sortedRight(rightIdx))
        }
        if (takeLeft) {
          output += sortedLeft(leftIdx)
          leftIdx += 1
        } else {
          output += sortedRight(rightIdx)
          rightIdx += 1
        }
      }

      output.result()
    }

}
