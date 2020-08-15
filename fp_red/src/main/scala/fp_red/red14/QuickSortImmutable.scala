package fp_red.red14

object QuickSortImmutable {
  def noop[S] = ST[S, Unit](())

  def partition[S](a: STArray[S, Int], l: Int, r: Int, pivot: Int): ST[S, Int] =
    for {
      pv <- a.read(pivot)
      _  <- a.swap(pivot, r)
      j  <- STRef(l)
      _  <- (l until r).foldLeft(noop[S])((s, i) => for {
        _  <- s
        vi <- a.read(i)
        _  <- if (vi < pv) for {
          vj <- j.read
          _  <- a.swap(i, vj)
          _  <- j.write(vj + 1)
        } yield () else noop[S]
      } yield ())
      x  <- j.read
      _  <- a.swap(x, r)
    } yield x

  def qs[S](a: STArray[S,Int], l: Int, r: Int): ST[S, Unit] = if (l < r)
    for {
      pi <- partition(a, l, r, l + (r - l) / 2)
      _  <- qs(a, l, pi - 1)
      _  <- qs(a, pi + 1, r)
    } yield () else noop[S]

  def quicksort(xs: List[Int]): List[Int] =
    if (xs.isEmpty) xs 
    else ST.runST(new RunnableST[List[Int]] {
      def apply[S] = for {
        arr    <- STArray.fromList(xs) // STArray[S, Int]
        size   <- arr.size
        _      <- qs(arr, 0, size - 1)
        sorted <- arr.freeze
      } yield sorted
    })

}
