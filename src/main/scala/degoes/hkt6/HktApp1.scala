package degoes.hkt6

/**
  * F[_] - no more than just a parameter
  * can be used as a parameter in:
  * - class
  * - interface
  * - function parameter
  * - function return type
  */
object HktApp1 extends App {

  case class Container[F](data: F)
  case class ContainerHK[F[_]](data: F[_])

  // normal
  val v1: Container[Int] = Container(1)
  val v2: Container[List[Int]] = Container(List(1,2,3))
  // high order
  val v3: ContainerHK[Comparable] = ContainerHK(1)        // Comparable[Int]
  val v4: ContainerHK[List] = ContainerHK(List("a"))      // List[String]
  val v5: ContainerHK[Option] = ContainerHK(Option(true)) // Option[Boolean]
}
