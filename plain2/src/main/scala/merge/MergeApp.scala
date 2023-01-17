package merge

import scala.reflect.ClassTag

object MergeApp extends App {

  /** interface which works with any collection of any type */
  trait CCOps[CC[_]] {
    def default[A]: A
    def create[A](size: Int)(implicit ev: ClassTag[A]): CC[A]
    def length(cc: CC[_]): Int
    def get[A](cc: CC[A], idx: Int): A
    def set[A](cc: CC[A], idx: Int, a: A): Unit
  }

  /** merge implementation */
  def merge[A: ClassTag, CC[_]](a1: CC[A], a2: CC[A])(implicit cmp: Ordering[A], ff: CCOps[CC]): CC[A] = {
    val size1: Int = ff.length(a1)
    val size2: Int = ff.length(a2)
    val a3: CC[A] = ff.create(size1 + size2)

    var i = 0
    var j = 0
    var k = 0

    while (i < ff.length(a1) && j < ff.length(a2)) {
      ff.set(
        a3,
        k, {
          val ai = ff.get(a1, i)
          val bj = ff.get(a2, j)

          if (cmp.compare(ai, bj) < 0) {
            i += 1
            ai
          } else {
            j += 1
            bj
          }
        }
      )
      k += 1
    }

    while (i < ff.length(a1)) {
      ff.set(a3, k, ff.get(a1, i))
      k += 1
      i += 1
    }

    while (j < ff.length(a2)) {
      ff.set(a3, k, ff.get(a2, j))
      k += 1
      j += 1
    }

    a3
  }

  /** set of functions to deal with Array[_] */
  implicit val iaCCOps: CCOps[Array] = new CCOps[Array] {
    override def default[A]: A = null.asInstanceOf[A]
    override def create[A](size: Int)(implicit ev: ClassTag[A]): Array[A] = Array.fill(size)(default)
    override def length(cc: Array[_]): Int = cc.length
    override def get[A](cc: Array[A], idx: Int): A = cc(idx)
    override def set[A](cc: Array[A], idx: Int, a: A): Unit = cc(idx) = a
  }

  val a1: Array[Int] = Array(2, 4, 6, 100, 200)
  val a2: Array[Int] = Array(1, 3, 5, 7, 9)
  val a3: Array[Int] = merge(a1, a2)

  println(a3.mkString("Array(", ", ", ")"))

}
