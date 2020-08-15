package fp_red.red14

object Playground extends App {
  /**
    * forEach via foldLeft
    */
  val r: Unit = List(1,2,3).foldLeft(())((_, x) => pprint.pprintln(x))
}
