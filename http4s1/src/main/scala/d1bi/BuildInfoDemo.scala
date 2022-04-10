package d1bi

import alexr.BuildInfo

object BuildInfoDemo extends App {

  val buildInfo = BuildInfo
  buildInfo.toMap.foreach { case (k, v) => println(s"$k: $v") }

}
