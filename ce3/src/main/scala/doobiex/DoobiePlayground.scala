package doobiex

import cats.effect.IO
import cats.effect.IOApp
import doobie._
import doobie.implicits._
import enumeratum.CirceEnum
import enumeratum.DoobieEnum
import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait AchievementType extends EnumEntry with Hyphencase

object AchievementType extends Enum[AchievementType] with CirceEnum[AchievementType] with DoobieEnum[AchievementType] {

  override def values: IndexedSeq[AchievementType] = findValues

  case object Milestone extends AchievementType
  case object BestMonth extends AchievementType

}

object DoobiePlayground extends IOApp.Simple {

  val at1: AchievementType = AchievementType.Milestone
  val at2: AchievementType = AchievementType.BestMonth

  def insert(at: AchievementType) =
    sql"insert into atype (a) values ($at)".update.run

  def select() =
    sql"select a from atype"
      .query[AchievementType]
      .to[Vector]

  val progInsert = for {
    _ <- insert(at1)
    _ <- insert(at2)
  } yield ()

  val appSelect = for {
    xs <- select().transact(xa[IO])
    _  <- IO(println(xs))
  } yield ()

//  override def run: IO[Unit] = progInsert.transact(xa[IO])
  override def run: IO[Unit] = appSelect
}
