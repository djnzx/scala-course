package cats101

object C165FoldableIntro extends App {
  val data = List(1,2,3,4)
  println(data)
  println(data.foldLeft(Nil: List[Int])((list, el) => el::list))  // inversion
  println(data.foldRight(Nil: List[Int])((el, list) => el::list)) // copy

  /**
    * sum
    * via foldRight
    */
  def sum[A](xs: List[A])(implicit ev: Numeric[A]): A =
    xs.foldRight(ev.zero)((acc, a) => ev.plus(acc, a))
  def sum2[A](xs: List[A])(implicit ev: Numeric[A]): A =
    xs.foldRight(ev.zero)(ev.plus(_, _))
  def sum3[A](xs: List[A])(implicit ev: Numeric[A]): A =
    xs.foldRight(ev.zero)(ev.plus)
  /**
    * filter
    * via foldRight
    */
  def filter(p: Int => Boolean)(xs: List[Int]): List[Int] =
    xs.foldRight(Nil: List[Int])((a, as) => if (p(a)) a::as else as)
  /**
    * map
    * via foldRight
    */
  def map(f: Int => Int)(xs: List[Int]): List[Int] =
    xs.foldRight(Nil: List[Int])((a,bs) => f(a)::bs)
  /**
    * flatMap
    * via foldRight
    */
  def flatMap(g: Int => List[Int])(xs: List[Int]): List[Int] =
    xs.foldRight(Nil: List[Int])((a, as) => g(a) ::: as)

  println(sum(data))         // 10

  val gt2 = (a: Int) => a > 2
  println(filter(gt2)(data))  // List(3, 4)

  val plus1 = (a: Int) => a + 1
  println(map(plus1)(data))  // List(2,3,4,5)

  val ffm = (a: Int) => List(a + 10, a + 20)
  println(flatMap(ffm)(data)) // List(11, 21, 12, 22, 13, 23, 14, 24)
}
