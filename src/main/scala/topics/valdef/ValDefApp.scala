package topics.valdef

object ValDefApp extends App {

  val valAdder: (Int => Int) => Int => Int = f => x => f(x)
  def defAdder[A](f: A => A)(x: A): A = f(x)

  def methodA[A] = {
    val valAdder2: (A => A) => A => A = f => x => f(x)
  }

  methodA

}
