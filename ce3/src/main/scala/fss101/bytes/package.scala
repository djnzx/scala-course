package fss101

import com.comcast.ip4s.IpLiteralSyntax

package object bytes {

  implicit class SizeSyntax(x: Int) {
    def kb: Int = x * 1024
    def mb: Int = x * 1024 * 1024
  }

  val host = host"localhost"
  val port = port"5000"

}
