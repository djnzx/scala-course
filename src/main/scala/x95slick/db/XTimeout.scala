package x95slick.db

import scala.concurrent.duration.Duration

trait XTimeout {
  val timeout: Duration
}
