package a

/**
  * https://stackoverflow.com/questions/15202997/what-is-the-difference-between-canonical-name-simple-name-and-class-name-in-jav
  */
object App1 extends App {
  
  /** on the one hand I can Write, but nobody will check it */
  val clazz1 = "a.b.c.ImplementationXofInterfaceY"
  
  /** we need to use: */
  val clazz2 = classOf[a.b.c.ImplementationXofInterfaceY].getCanonicalName
  
  println(clazz1)
  println(clazz2)
  
  /** and proper configuration should look like this */
  val props = Map(
    "bla.bla.bla" -> classOf[a.b.c.ImplementationXofInterfaceY].getCanonicalName
  )
}
