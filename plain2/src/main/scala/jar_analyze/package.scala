import java.io.{BufferedWriter, File, FileWriter}

package object jar_analyze {

  val path = "jar_analyze"
  val pathDst = "scala_plain/src/main/resources/jar_analyze/out"
  val jarOozieSparkInitial = "jars_oozie_spark_initial.txt"
  val jarSpark = "jars_spark247.txt"
  val jarOozieLib = "jars_oozie_lib.txt"
  val jarOozieLibExt = "jars_oozie_libext.txt"

  def obtainResource(fileName: String) =
    Option(getClass.getClassLoader.getResource(s"$path/$fileName"))
      .map(_.getFile)
      .map(new File(_))
  
  def writeFile[A](name: String, contents: Iterable[A]) = {
    val w = new BufferedWriter(new FileWriter(new File(s"$pathDst/$name")))
    contents.foreach { x => 
      w.write(x.toString)
      w.write("\n")
    }
    w.close()
  }
  
}
