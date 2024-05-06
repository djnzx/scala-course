package fss.d5http4stream

import io.circe.generic.AutoDerivation

case class Data(value: Int)
case object Data extends AutoDerivation
