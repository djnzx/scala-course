package warmup.nth

class NthScala {
  def calculate(m: Int): Int = (LazyList from 1)
    .filter(n => (n % 3 == 0 ) || (n % 4 == 0)) // filter by rule
    .filter(n => n % 2 != 0 )                   // odd only
    .zip(LazyList from 1)                      // count
    .filter(t => t._2 == m)                     // take n-th element
    .head                                      // take head
    ._1                                        // unpack tuple
}
