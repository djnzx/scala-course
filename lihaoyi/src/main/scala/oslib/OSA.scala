package oslib

import os.{CommandResult, Path}

object OSA extends App {
  val wd: Path = os.pwd
  println(os.home) // /Users/alexr
  println(os.pwd)  // /Users/alexr/dev/_learn/_scala/learn-scala-cook-book-aa
  println(os.rel)  // empty
  println(os.root) // /
  println(os.sub)  // empty
  println(os.up)   // ..
//  os.write(
//    wd/"all.txt",
//    os.list(wd).filter(_.ext == "txt").map(os.read)
//  )
//  val total = os.walk(os.pwd).foldLeft(0L)((a, p) => a + os.size(p))
//  println(total)
  val invoked: CommandResult = os.proc("cat", wd/"build.sbt").call()
  println(invoked)
}
