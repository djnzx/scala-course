package inmemoryclassloader

import java.io.{BufferedReader, ByteArrayInputStream, InputStream, InputStreamReader}

object InMemoryClassLoader extends App {

  val classLoader = new ClassLoader() {
    val data = Map("test1" -> "ABC123".getBytes)

    override def getResourceAsStream(name: String): InputStream =
      data.get(name) match {
        case None => super.getResourceAsStream(name)
        case Some(bytes) => new ByteArrayInputStream(bytes)
      }
  }

  val is: InputStream = classLoader
//    .getResourceAsStream("tpl/a.txt")
    .getResourceAsStream("test1")

  val br = new BufferedReader(new InputStreamReader(is))
  br.lines().forEach(println)
  br.close()

}
