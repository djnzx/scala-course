package security

import java.security.MessageDigest

// typeclass
sealed trait Algorithm[A <: Algorithm.Tag] {
  def instance: MessageDigest
}

object Algorithm {
  // tags
  // we can't use case object here
  sealed trait Tag
  object Tag {
    sealed trait MD5  extends Tag
    sealed trait SHA1 extends Tag
  }

  private def make[A <: Tag](name: String): Algorithm[A] =
    new Algorithm[A] {
      def instance: MessageDigest = java.security.MessageDigest.getInstance(name)
    }
  // specific implementations
  implicit val md5: Algorithm[Tag.MD5] = make("MD5")
  implicit val sha1: Algorithm[Tag.SHA1] = make("SHA1")

  // implementation accessor
  def apply[A <: Tag](implicit ev: Algorithm[A]): MessageDigest =
    implicitly[Algorithm[A]].instance
}

object Test extends App {
  val datas = "hello"
  val data = datas.getBytes("UTF-8")

  def hex(bytes: Array[Byte]) = bytes.map(b => "%02X".format(b)).mkString

  import Algorithm.Tag._

  val bytes1 = Algorithm[MD5].digest(data)
  val bytes2 = Algorithm[SHA1].digest(data)

  pprint.log(datas)
  pprint.log(hex(data))
  pprint.log(hex(bytes1))
  pprint.log(hex(bytes2))

}
