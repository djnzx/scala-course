package app

import cats.effect.IOApp

trait MyIOApp extends IOApp.Simple {

  override protected def computeWorkerThreadCount: Int = 2

}
