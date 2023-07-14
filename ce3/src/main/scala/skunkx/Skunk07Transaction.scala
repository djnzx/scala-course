package skunkx

import cats.effect.IO
import cats.effect._
import cats.implicits._
import skunk._
import skunk.codec.all._
import skunk.implicits._

case class Pet(name: String, age: Short)

trait PetService[F[_]] {
  def tryInsertAll(pets: List[Pet]): F[Unit]
  def selectAll: F[List[Pet]]
}

object PetService {

  // command to insert a pet
  private val insertOne: Command[Pet] =
    sql"INSERT INTO pets VALUES ($varchar, $int2)"
      .command
      .to[Pet]

  // query to select all pets
  private val all: Query[Void, Pet] =
    sql"SELECT name, age FROM pets"
      .query(varchar *: int2)
      .to[Pet]

  // construct a PetService, preparing our statement once on construction
  def fromSession(s: Session[IO]): IO[PetService[IO]] =
    s.prepare(insertOne).map { pc =>
      new PetService[IO] {

        // Attempt to insert all pets, in a single transaction, handling each in turn and rolling
        // back to a savepoint if a unique violation is encountered. Note that a bulk insert with an
        // ON CONFLICT clause would be much more efficient, this is just for demonstration.
        def tryInsertAll(pets: List[Pet]): IO[Unit] =
          s.transaction.use { xa =>
            pets.traverse_ { p =>
              for {
                _  <- IO.println(s"Trying to insert $p")
                sp <- xa.savepoint
                _  <- pc.execute(p).recoverWith {
                  case SqlState.UniqueViolation(ex) =>
                    IO.println(s"Unique violation: ${ex.constraintName.getOrElse("<unknown>")}, rolling back...") *>
                      xa.rollback(sp)
                }
              } yield ()
            }
          }

        def selectAll: IO[List[Pet]] = s.execute(all)
      }
    }

}

object Skunk07Transaction extends SkunkExploreApp {

  // a resource that creates and drops a temporary table
  def withPetsTable(s: Session[IO]): Resource[IO, Unit] = {
    val alloc = s.execute(sql"CREATE TEMP TABLE pets (name varchar unique, age int2)".command).void
    val free  = s.execute(sql"DROP TABLE pets".command).void
    Resource.make(alloc)(_ => free)
  }

  def withTransactionStatusLogger[A](ss: Session[IO]): Resource[IO, Unit] = {
    val alloc: IO[Fiber[IO, Throwable, Unit]] =
      ss.transactionStatus
        .discrete
        .changes
        .evalMap(s => IO.println(s"xa status: $s"))
        .compile
        .drain
        .start
    Resource.make(alloc)(_.cancel).void
  }

  val resource: Resource[IO, PetService[IO]] =
    for {
      s <- sessionR
      _ <- withPetsTable(s)
      _ <- withTransactionStatusLogger(s)
      ps <- Resource.eval(PetService.fromSession(s))
    } yield ps

  val pets = List(
    Pet("Alice", 3),
    Pet("Bob", 42),
    Pet("Bob", 21),
    Pet("Steve", 9)
  )

  override val app: IO[Unit] =
    resource.use { ps =>
      for {
        _ <- ps.tryInsertAll(pets)
        all <- ps.selectAll
        _ <- all.traverse_(p => IO.println(p))
      } yield ExitCode.Success
    }
}
