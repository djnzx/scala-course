package app

object DetectScalaCompilerVersion extends App {

  lazy val compilerVersion = {
    val props = new java.util.Properties
    val st = getClass.getResourceAsStream("/library.properties")
    props.load(st)
    st.close()
    Option(props.getProperty("version.number"))
  }

  println(compilerVersion.getOrElse("not detected"))

}
