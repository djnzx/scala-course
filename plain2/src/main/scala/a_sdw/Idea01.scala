package a_sdw

object Idea01 {
  sealed trait Result[+E, +A]
  final case class RSuccess[A](a: A) extends Result[Nothing, A]
  final case class RFatal[E](e: E) extends Result[E, Nothing]
  final case class RWarning[A, E](a: A, e: E) extends Result[E, A] 
  
  trait R
  trait Body
  trait A {
    def a(body: Body): R
  }
  trait B {
    def b(body: Body): R
  }
  trait C {
    def c(body: Body): R
  }

  def transformer1(body: Body, ts: A with B): Seq[Result[Vector[String], R]] = Seq(
    RSuccess(ts.a(body)),
    RWarning(ts.b(body), Vector("Error #1"))
  )
  def transformer2(body: Body, ts: B with C) = Seq(
    ts.b(body),
    ts.c(body)
  )

}
