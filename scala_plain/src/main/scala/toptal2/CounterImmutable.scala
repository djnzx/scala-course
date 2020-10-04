package toptal2

class CounterImmutable[A](private val m: Map[A, Int] = Map.empty[A, Int]) {
  def inc(k: A) = new CounterImmutable(m.updatedWith(k) {
    case None    => Some(1)
    case Some(n) => Some(n+1)
  })
  def dec(k: A) = new CounterImmutable(m.updatedWith(k) {
    case None |
         Some(1) => None
    case Some(n) => Some(n-1)
  })
  def get(k: A) = m.getOrElse(k, 0)
  def size = m.size
  override def toString() = m.toString()
}
