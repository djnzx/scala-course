package aa_fp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Fps116A extends App {

  println("future is going to be created")
  // actually that line not only defines future but also runs it
  val a = Future { println("FS"); Thread.sleep(1000); println("FF"); 42 }
  println("future built")
  a.onComplete {
    case Success(value) => println(s"done. value = ${value}")
    case Failure(_)    => println("Exception was")
  }
  println("going to sleep...")
  Thread.sleep(2000)
  println("... woke up")
}
