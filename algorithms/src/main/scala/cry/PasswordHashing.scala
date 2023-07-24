package cry

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.{PasswordEncoder, Pbkdf2PasswordEncoder}
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm

object PasswordHashing extends App {

  val encoder1: PasswordEncoder = new Pbkdf2PasswordEncoder("this is secret", 32, 17, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512)
  val encoder2: PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8

  val pass = "hello"

  println(encoder1.encode(pass))
  println(encoder2.encode(pass))
}
