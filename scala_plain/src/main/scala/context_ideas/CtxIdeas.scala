package context_ideas

object CtxIdeas extends App {
  
  sealed trait Context
  final case class ContextA(a: Int) extends Context
  final case class ContextB(b: Double) extends Context
  
  def doSomething(implicit ctx: Context) = ctx match {
    case ContextA(a) => println(s"Context A given $a")
    case ContextB(b) => println(s"Context B given $b")
  }
  
  def useCase1 = {
    implicit val ctx = ContextA(11)
    doSomething
  }
  
  def useCase2 = {
    implicit val ctx = ContextB(3.14)
    doSomething
  }
  
  useCase1
  useCase2

}
