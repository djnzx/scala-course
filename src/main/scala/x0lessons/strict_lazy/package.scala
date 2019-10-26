package x0lessons

package object strict_lazy {

  val data: List[Int] = List(1, 25, 40, 5, 23)

  def lessThan30(i: Int): Boolean = {
    println(s"$i less than 30?")
    i < 30
  }

  def moreThan20(i: Int): Boolean = {
    println(s"$i more than 20?")
    i > 20
  }

}
