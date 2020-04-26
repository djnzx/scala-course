package hackerrankfp.d200426_05

object SuperDigitApp {

  @scala.annotation.tailrec
  def super_digit(n: String): Int = n.length match {
    case 1 => n.toInt
    case _ => super_digit(n.toList.map(_-'0').sum.toString)
  }

  def process(main: String, rep: Int) =
    super_digit((super_digit(main)*rep).toString)

  def body(readLine: => String): Unit = {
    val a = readLine.split(" ")
    val r = process(a(0), a(1).toInt)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/superd.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
