package toptal.q2.twopointers

import toptal.q2.CounterImmutable

object TwoPointersRecursiveImmutable extends TwoPointers {
  def find[A](a: IndexedSeq[A], k: Int): Int = {
    if (a.isEmpty) return 0

    case class State(i: Int = 0, j: Int = -1, l: Int = 0, r: Int = Int.MaxValue, c: CounterImmutable[A] = new CounterImmutable[A]) {
      private def updateRL = if (j - i < r - l) copy(l = i, r = j) else this
      private def pull(st: State): State = (st.i, st.c) match {
        case (i, c) if c.get(a(i)) > 1 => pull(st.copy(i = i + 1, c = c.dec(a(i))))
        case _ => st
      }
      def pullTail = pull(this).updateRL
      def moveBy1 = copy(j = j + 1, c = c.inc(a(j + 1))).pullTail
      def done = len == k || j >= a.length-1
      def tryToRelax = processToEnd(this)
      def len = r - l + 1
    }

    def findFirst(st: State = State()): State = st.c.size match {
      case `k` => st.copy(r = st.j).pullTail
      case _   => findFirst(st.moveBy1)
    }
    
    def processToEnd(st: State): State = st.done match {
      case true  => st
      case false => processToEnd(st.moveBy1)
    }

    findFirst().tryToRelax.len
  }
}
