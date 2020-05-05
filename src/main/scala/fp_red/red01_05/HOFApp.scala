package fp_red.red01_05

object HOFApp extends App {



  type ItoI = Int => Int

  val inc: ItoI = (i: Int) => i + 1
  val double: ItoI => ItoI = (f: ItoI) => f compose f
  val unit7: ItoI = _ => 7

  println(double(inc)(7))
  println(unit7(5))



}
