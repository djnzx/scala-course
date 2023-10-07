package gas104

import cats.effect.IO
import cats.effect.IOApp
import io.circe.generic.AutoDerivation
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}

object GasApp extends IOApp.Simple {


  override def run: IO[Unit] = IO{

    println(

    )
  }
}
