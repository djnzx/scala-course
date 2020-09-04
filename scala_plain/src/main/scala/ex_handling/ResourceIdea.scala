package ex_handling

import java.io.FileInputStream

import scala.util.Using

object ResourceIdea extends App {
  
  // acquire
  // use
  // release
  // handle errors
  // Using
  class Resource[A, X <: Throwable](acquire: => A)(release: A => Unit) {
    def use[B](body: A => B)(handler: X => B): B = {
      val resource: A = acquire
      try {
        body(resource)
      } catch {
        case x: X => handler(x)
      } finally {
        release(resource)
      }
    }
  }
  
  val fis = new Resource(new FileInputStream("1.txt"))(_.close())
  val r: Int = fis.use(is => is.read())(_ => -13)
  pprint.log(r)

}
