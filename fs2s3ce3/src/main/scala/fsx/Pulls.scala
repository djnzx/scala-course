package fsx

import cats.Id
import fs2.*
import cats.effect.*
import cats.effect.unsafe.implicits.global

/** s3fs/console */
object Pulls extends App {

  val helloStream: Stream[Pure, String] = Stream("hello")

  val helloPull: Pull[Pure, String, Unit] = Pull.output1("hello")

  val helloStream2: Stream[Pure, String] = helloPull.stream

  val out1: List[String] = helloStream.compile.toList

  val out2: List[String] = helloPull.stream.compile.toList

  val empty = Pull.done.stream // Stream.empty

  helloStream.pull.echo

  helloStream.pull.echo.stream.compile.toList
  // res12: List[String] = List("hello")
  helloStream.pull.echo.stream.pull.echo.stream.compile.toList
  // res13: List[String] = List("hello")
  helloStream.compile.toList
  // res14: List[String] = List("hello")

}
