import com.comcast.ip4s.IpLiteralSyntax

package object bytes {

  implicit class SizeSyntax(x: Int) {
    def k: Int = x * 1024
    def m: Int = x * 1024 * 1024
  }

  val host = host"localhost"
  val port = port"5000"

}
