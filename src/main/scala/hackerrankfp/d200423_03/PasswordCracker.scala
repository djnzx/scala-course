package hackerrankfp.d200423_03

/**
  * https://www.hackerrank.com/challenges/password-cracker/problem
  * did not execute within the time limits...
  */
object PasswordCracker extends App {

  def passwordCracker(pwds: Seq[String], attempt: String): String = {
    def crack(part: String, parts: List[String]): Option[List[String]] = part match {
      case "" => Some(parts)
      case _ => if ((List(attempt).flatten.toSet -- pwds.flatten.toSet).nonEmpty) None
      else pwds
        .flatMap { pwd => if (pwd.length <= part.length && pwd == part.substring(0, pwd.length)) Some(pwd) else None }
        .flatMap { sub => crack(part.substring(sub.length), sub::parts) }
        .headOption
    }
    crack(attempt, Nil) map { _.reverse.mkString(" ") } getOrElse "WRONG PASSWORD"
  }

  val pwds = List(
//    "because", "can", "do", "must", "we", "what"
    "a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa"
  )
  val attempt =
    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
//  "wedowhatwemustbecausewecan"
  val r = passwordCracker(pwds, attempt)
  println(r)
}
