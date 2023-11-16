package primes

object PrimesApp extends App {

  def divisible(a: Int, b: Int): Boolean = a % b == 0

  def sieve(xs: LazyList[Int]): LazyList[Int] =
    LazyList(xs.head)
      .lazyAppendedAll(
        sieve(xs.tail.filter(x => !divisible(x, xs.head)))
      )

  def primes: LazyList[Int] = sieve(LazyList.from(2))

  println((primes take 1000).toList)

}
