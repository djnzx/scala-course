package genextra

trait MkInfinite {

  implicit class InfiniteOps[A](xs: Seq[A]) {
    private lazy val seqLazy = LazyList.from(xs)

    def infinite: LazyList[A] =
      seqLazy lazyAppendedAll infinite
  }

}
