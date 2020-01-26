package aa_cookbook.x001

object RegExpReplace extends App {
  val pattern = "[0-9]+".r
  val address = "228 47th street, Brooklyn, New York, 11220"
  pattern.findFirstMatchIn(address)
  val replaced = pattern.replaceAllIn(address, "X")

  println(replaced)
}
