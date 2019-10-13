package __udemy.scala_beginners.lectures.part3fp._try_succ_fail

import scala.util.{Failure, Success, Try}

/**
  * Created by Daniel.
  */
object HandlingFailure extends App {

  // basic
  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("SUPER FAILURE"))

  println(aSuccess)
  printf("success is instance of Try: %s\n", aSuccess.isInstanceOf[Try[Any]])
  printf("success is instance of Success: %s\n", aSuccess.isInstanceOf[Success[Any]])
  printf("success is instance of Failure: %s\n", aSuccess.isInstanceOf[Failure[Any]])

  println(aFailure)
  printf("failure is instance of Try: %s\n", aFailure.isInstanceOf[Try[Any]])
  printf("failure is instance of Success: %s\n", aFailure.isInstanceOf[Success[Any]])
  printf("failure is instance of Failure: %s\n", aFailure.isInstanceOf[Failure[Any]])

  def unsafeMethod(): String = throw new RuntimeException("NO STRING FOR YOU BUSTER")
  // will break
  // unsafeMethod()

  val potentialFailure = Try(unsafeMethod()) // apply
  printf("PF: %s\n", potentialFailure)

  // syntax sugar
  val anotherPotentialFailure = Try {
    // code that might throw
  }

  println(s"PF.isSuccess: ${potentialFailure.isSuccess}")

  // orElse
  def backupMethod(): String = "A backup valid result"
  println(s"backup: $backupMethod")
  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))
  println(s"fallbackTry: $fallbackTry")

  // IF you design the API
  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)
  def betterBackupMethod(): Try[String] = Success("A valid result")
  val betterFallback = betterUnsafeMethod() orElse betterBackupMethod()

  def divisor(a: Int, b:Int):Try[Int] = b match {
      case 0 => Failure(new RuntimeException)
      case _ => Success(a/b)
    }

  val r0 = Try(2/0)
  println(r0) // Failure(java.lang.ArithmeticException: / by zero)
  val r1 = divisor(2, 0)
  println(r1.isSuccess) // true
  val r2 = divisor(2, 2)
  println(r2.isSuccess) // false

  // map, flatMap, filter
  println(aSuccess.map(_ * 2)) // 3 -> 6
  println(aSuccess.flatMap(x => Success(x * 10))) //3 -> 30
  println(aSuccess.filter(_ > 10)) // Failure(java.util.NoSuchElementException: Predicate does not hold for 3)
}
