package app

import scala.beans.BeanProperty

case class Person(
  @BeanProperty id: Int,
  @BeanProperty name: String
)
