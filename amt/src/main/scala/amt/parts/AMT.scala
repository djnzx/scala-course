package amt.parts

import amt.parts.generated.UserServiceServiceLocator

case class Credentials(user: String, password: String) {
  lazy val asArray: Array[AnyRef] = Array(user, password)
}

object Credentials {
  def fromEnv: Credentials = Credentials(
    System.getenv("AMT_LOGIN"),
    System.getenv("AMT_PASSWD"),
  )
}

class AMT(cred: Credentials) {

  private val api = new UserServiceServiceLocator().getUserServicePort

  def fetchNumberDetails(oems: Array[String]): Array[PartNumber] =
    api
      .getPriceByOem(
        oems.asInstanceOf[Array[AnyRef]],
        cred.asArray,
      )
      .map(x => PartNumber.parse(x.asInstanceOf[java.util.Map[String, Object]]))

  def fetchNumberDetails(oem: String): Array[PartNumber] =
    fetchNumberDetails(Array(oem))

  def fetchNumberDetails(oems: Iterable[String]): Array[PartNumber] =
    fetchNumberDetails(oems.toArray)

}
