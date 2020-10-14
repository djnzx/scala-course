package toptal.q2

import scala.collection.mutable

class Counter[A] (private val m: mutable.Map[A, Int] = mutable.Map.empty[A, Int]) {
  def inc(k: A) = m.updateWith(k) {
    case None    => Some(1)
    case Some(n) => Some(n+1)
  }
  def dec(k: A) = m.updateWith(k) {
    case None |
         Some(1) => None
    case Some(n) => Some(n-1)
  }
  def get(k: A) = m(k)
  def size() = m.size
  override def toString() = m.toString()
}
