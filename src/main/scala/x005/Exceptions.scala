package x005

import java.io.IOException

object Exceptions extends App {

  class Ex {
    @throws(classOf[IOException]) // only for Java interop
    def play {}
  }

}
