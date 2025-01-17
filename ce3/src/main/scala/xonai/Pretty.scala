package xonai

import xonai.Main.StringColumn

object Pretty {

  def pad3(x: Any) = "%3s".formatted(x)

  def show(sc: StringColumn) = Seq(
    sc.buffer.indices.map(pad3),
    sc.buffer.map(_.toChar).map(pad3).toSeq,
    sc.buffer.indices.map {
      case idx if sc.offset.contains(idx) => "  ^"
      case _                              => "   "
    },
  ).foreach(x => println(x.mkString))

}
