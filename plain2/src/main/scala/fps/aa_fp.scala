package fps

package object aa_fp {

  // that's no more than String result after getLn.run
  val getLn: XIO[String] = XIO(scala.io.StdIn.readLine)
  val putLn: String => XIO[Unit] = (line: String) => XIO(scala.Console.println(line))
  val quit: XIO[Unit] = XIO(())

}
