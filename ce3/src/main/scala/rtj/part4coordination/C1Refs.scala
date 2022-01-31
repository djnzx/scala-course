package rtj.part4coordination

import cats.effect.IO
import cats.effect.Ref

/** Ref is just a functional wrapper over Java Atomic Reference */
object C1Refs {

  // ref = purely functional atomic reference
  val atomicMol: IO[Ref[IO, Int]] = Ref[IO].of(42)
  val atomicMol_v2: IO[Ref[IO, Int]] = IO.ref(42)

  // modifying is an effect
  val increasedMol1: IO[IO[Unit]] = atomicMol.map { ref: Ref[IO, Int] =>
    ref.set(43) // thread-safe
  }
  val increasedMol: IO[Unit] = atomicMol.flatMap { ref: Ref[IO, Int] =>
    ref.set(43) // thread-safe
  }

  // obtain a value
  val mol = atomicMol.flatMap { ref =>
    ref.get // thread-safe
  }

  val gsMol: IO[Int] = atomicMol.flatMap { ref: Ref[IO, Int] =>
    ref.getAndSet(43)
  } // gets the old value, sets the new one

  // updating with a function
  val fMol: IO[Unit] = atomicMol.flatMap { ref =>
    ref.update(value => value * 10)
  }

  val updatedMol: IO[Int] = atomicMol.flatMap { ref =>
    ref.updateAndGet(value => value * 10) // get the new value
  // can also use getAndUpdate to get the OLD value
  }

  // modifying with a function returning a different type
  val modifiedMol: IO[String] = atomicMol.flatMap { ref =>
    ref.modify(value => (value * 10, s"my current value is $value"))
  }

}
