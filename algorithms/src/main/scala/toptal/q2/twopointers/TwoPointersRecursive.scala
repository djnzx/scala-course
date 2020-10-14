package toptal.q2.twopointers

import toptal.q2.Counter

object TwoPointersRecursive extends TwoPointers {
  def find[A](a: IndexedSeq[A], k: Int): Int = {
    if (a.isEmpty) return 0
    val N = a.length
    val c = new Counter[A]

    case class State(i: Int = 0, j: Int = -1, l: Int = 0, r: Int = Int.MaxValue) {
      def len = r - l + 1
      def relax = if (j - i < r - l) copy(l = i, r = j) else this
      def pullTail = {
        var ii = i
        while (c.get(a(ii)) > 1) {
          c dec a(ii)
          ii += 1
        }
        copy(i = ii).relax
      }
      def moveBy1 = {
        c inc a(j+1)
        copy(j = j + 1)
      }
      def done = r-l+1 == k || j >= N-1
      def doRelax_ = doRelax(this)
    }

    def findFirst(st: State = State()): State = c.size() match {
      case `k` => st.copy(r = st.j).pullTail
      case _ =>
        c inc a(st.j + 1)
        findFirst(st.copy(j = st.j + 1))
    }
    
    def doRelax(st: State): State = st.done match {
      case true  => st
      case false => doRelax(st.moveBy1.pullTail)
    }

    findFirst().doRelax_.len
  }
}
