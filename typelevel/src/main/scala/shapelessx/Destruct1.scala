package shapelessx

object Destruct1 {
  
  import shapeless._
  import HList._
  import syntax.std.traversable._

  val x: Seq[AnyVal] = List(1, 2, 3)
  val y: Option[Int :: Double :: HNil] = x.toHList[Int::Double::HNil]
  // actually unsafe operation
  val z: (Int, Double) = y
    .get    // unpack option to HList
    .tupled // convert HList to tuple

  // fixed arity syntax
  def f(x:Int, y:Double): Double = x * y

  val result5: Double = f _ tupled z
  println(result5)

}
