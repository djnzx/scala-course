package hackerrankfp.d200423_03

/**
  * https://www.hackerrank.com/challenges/password-cracker/problem
  * did not execute within the time limits...
  */
object PasswordCracker extends App {


  def passwordCracker(pwds: Seq[String], attempt: String): String = {

    def crack0(part: String, parts: List[String]): Option[List[String]] =
      if (part.isEmpty) Some(parts) else
      pwds.sortWith { (s1, s2) => s2.length < s1.length }
        .flatMap { pwd => if (pwd.length <= part.length && pwd == part.substring(0, pwd.length)) Some(pwd) else None }
        .flatMap { sub => crack(part.substring(sub.length), sub :: parts) }
        .headOption

    def crack(part: String, parts: List[String]): Option[List[String]] =
      if (part.isEmpty) Some(parts) else
        pwds.sortWith { (s1, s2) => s2.length < s1.length }
          .flatMap { pwd => if (pwd.length <= part.length && pwd == part.substring(0, pwd.length)) Some(pwd) else None }
          .foldLeft((true, Option.empty[List[String]])) { (acc, sub) =>
            if (!acc._1) acc else
            crack(part.substring(sub.length), sub :: parts) match {
              case Some(value) => (false, Some(value))
              case None        => (true, None)
            }
          }._2

    ((List(attempt).flatten.toSet -- pwds.flatten.toSet).nonEmpty match {
      case true  => None
      case false => crack(attempt, Nil) map { _.reverse.mkString(" ") }
    }) getOrElse "WRONG PASSWORD"
  }

  val pwds = List(
//    "because", "can", "do", "must", "we", "what"
    "a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa"
  )
  val attempt =
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
//  "wedowhatwemustbecausewecan"
  val r = passwordCracker(pwds, attempt)
  println(r)
}
