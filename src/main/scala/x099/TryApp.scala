package x099

import scala.util.{Failure, Success, Try}

object TryApp extends App {

  val try1 = Try(1+2) // Success
  val try2 = Try(2/0) // Failure

//  try1 match {
//    case Success(value) => println(value)
//    case Failure(error) => println(error)
//  }
//
//  try2 match {
//    case Success(value) => println(value)
//    case Failure(error) => println(error)
//  }

  val seq = Seq(try1, try2)
  // way1
  val seq2 = seq.flatMap(_.toOption)
  seq2.foreach(println)

  // way2
  seq.map(_.toOption).flatten
  // way3
  seq.filter(_.isSuccess).map(_.get)
  // way4
  seq.collect{case Success(x) => x}
  // way5
  val (successes, failures)=seq.partition(_.isSuccess)
  successes.map(_.get)
  failures.map(_.failed.get)
  failures.collect{case Failure(ex) => ex}


}
