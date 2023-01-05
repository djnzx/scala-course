import java.io.File
import java.net.URL

object ResourcesPlayground extends App {

  val c: Class[_] = getClass
  val cl: ClassLoader = c.getClassLoader
  val r: URL = cl.getResource("static/0")
  val f: String = r.getFile
  val path = f.substring(0, f.length-1)

  val f1 = new File(path + "1.txt")

  println(f1)
  println(f1.exists)

}
