package x00topics.fold

// https://oldfashionedsoftware.com/2009/07/30/lots-and-lots-of-foldleft-examples/
object FoldCollection extends App {

  def length[A](list: Seq[A]): Int = list.foldLeft(0)((ac, _) => ac + 1)
  def last[A](list: Seq[A]): A = list.foldLeft(list.head)((_,l) => l)
  def penultimate[A](list: Seq[A]): A = list.foldLeft(list.head -> list.tail.head)((r, c) => (r._2,c))._1
  def average(list: List[Double]): Double = list.foldLeft(0.0)(_+_) / list.foldLeft(0.0)((r,_) => r+1)

  val data = List(1,2,3,4,5,0)
  println(last(data))        // 0
  println(penultimate(data)) // 5
  println(length(data))      // 6
}
