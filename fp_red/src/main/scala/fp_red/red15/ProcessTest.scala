package fp_red.red15

import fp_red.red13.IO

object ProcessTest extends App {
  import GeneralizedStreamTransducers._
  import Process._

  val p = eval(IO { println("woot"); 1 }).repeat
  val p2 = eval(IO { println("cleanup"); 2 } ).onHalt {
    case Kill => println { "cleanup was killed, instead of bring run" }; Halt(Kill)
    case e => Halt(e)
  }

  println { Process.runLog { p2.onComplete(p2).onComplete(p2).take(1).take(1) } }
  println { Process.runLog(converter) }
//   println { Process.collect(Process.convertAll) }
}
