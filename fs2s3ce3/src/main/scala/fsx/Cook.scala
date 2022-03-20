package fsx

import cats.Id
import fs2.*
import cats.effect.*
import cats.effect.unsafe.implicits.global

object Cook extends App {

  type Dough = Int
  type Jiaozi = Int
  type Bowl = List[Jiaozi]
  type Leftovers = List[Jiaozi]

  // exactly three jiaozi can be made from each roll
  // If we make two rolls and serve four jiaozi, we should have a couple of jiaozi left over.

  /** roll(2).compile.toList */
  def roll(rollsToMake: Int): Stream[Pure, Dough] =
    Stream.iterate(0)(_ + 1).take(rollsToMake)

  /** roll(2).through(cook).compile.toList */
  val cook: Pipe[Pure, Dough, Jiaozi] =
    _.flatMap { dough =>
      Stream(
        dough * 3,
        dough * 3 + 1,
        dough * 3 + 2,
      )
    }

  /** roll(2).through(cook).through(serve(4)).compile.toList */
  def serve(jiaoziToServe: Int): Pipe[Pure, Jiaozi, Jiaozi] =
    _.take(jiaoziToServe)

  def serveThen(n: Int, store: Pipe[IO, Jiaozi, Nothing]): Pipe[IO, Jiaozi, Jiaozi] = ???

  type Box = Ref[IO, Leftovers]

  val emptyBox: IO[Box] = Ref.of(Nil)
  val box: Box = emptyBox.unsafeRunSync()

  box.update(leftovers => 3 :: leftovers).unsafeRunSync()
  box.get.unsafeRunSync()

  def store(box: Box): Pipe[IO, Jiaozi, Nothing] =
    _.evalMap(jiaozi => box.update(jiaozi :: _)).drain

  {
    for {
      box <- emptyBox
      _ <- Stream(1, 2, 3).through(store(box)).compile.drain
      leftovers <- box.get
    } yield leftovers
  }.unsafeRunSync()

  def sim(
      numberOfRolls: Int,
      jiaoziToServe: Int,
    ) =
    for {
      box <- emptyBox
      bowl = roll(numberOfRolls)
        .through(cook)
//        .through(serveThen(jiaoziToServe, store(box)))
        .through(serve(jiaoziToServe))
        .compile
        .toList
      leftovers <- box.get
    } yield (bowl, leftovers)

}
