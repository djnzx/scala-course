package mortals

import scalaz._
import Scalaz._

import scala.concurrent.duration._
import scala.util.Try

object Mortals003a {
  val s5: FiniteDuration = 5.seconds
  
  // Epoch + Duration = Epoch
  // Epoch - Epoch = Duration
  final case class Epoch(millis: Long) extends AnyVal {
    def +(d: FiniteDuration): Epoch = Epoch(millis + d.toMillis) 
    def -(e: Epoch): FiniteDuration = (millis - e.millis).millis
  }

  trait Drone[F[_]] {
    def getBacklog: F[Int]
    def getAgents: F[Int]
  }

  final case class MachineNode(id: String)

  trait Machines[F[_]] {
    def getTime: F[Epoch]
    def getManaged: F[NonEmptyList[MachineNode]]
    def getAlive: F[Map[MachineNode, Epoch]]
    def start(node: MachineNode): F[MachineNode]
    def stop(node: MachineNode): F[MachineNode]
  }

  final case class WorldView(
    backlog: Int,
    agents: Int,
    managed: NonEmptyList[MachineNode],
    alive: Map[MachineNode, Epoch],
    pending: Map[MachineNode, Epoch],
    time: Epoch
  )

  trait DynAgents[F[_]] {
    def initial: F[WorldView]
    def update(old: WorldView): F[WorldView]
    def act(world: WorldView): F[WorldView]
  }

  final class DynAgentsModule[F[_]: Monad](D: Drone[F], M: Machines[F]) extends DynAgents[F] {
    /** this is sequential implementation
      * we use flatMap only for accessing to the context */
    override def initial: F[WorldView] =
      for {
        db <- D.getBacklog
        da <- D.getAgents
        mm <- M.getManaged
        ma <- M.getAlive
        mt <- M.getTime
      } yield WorldView(db, da, mm, ma, Map.empty, mt)

    def initial_PAR: F[WorldView] =
      ^^^^(D.getBacklog, D.getAgents, M.getManaged, M.getAlive, M.getTime) {
        case (db, da, mm, ma, mt) => WorldView(db, da, mm, ma, Map.empty, mt)
      }
    // or
    def initial_PAR2: F[WorldView] =
      (D.getBacklog |@| D.getAgents |@| M.getManaged |@| M.getAlive |@| M.getTime) {
        case (db, da, mm, ma, mt) => WorldView(db, da, mm, ma, Map.empty, mt)
      }
    
    override def update(old: WorldView): F[WorldView] =
      for {
        snap <- initial
        changed = symdiff(old.alive.keySet, snap.alive.keySet)
        pending = (old.pending -- changed).filterNot {
          case (_, started) => (snap.time - started) >= 10.minutes
        }
        update = snap.copy(pending = pending)
      } yield update
      
    
    override def act(world: WorldView): F[WorldView] = world match {
      
      case NeedsAgent(node) =>
        for {
          _ <- M.start(node)
          update = world.copy(pending = Map(node -> world.time))
        } yield update
        
      case Stale(nodes) =>
        nodes.foldLeftM(world) { (world, n) =>
          for {
            _ <- M.stop(n)
            update = world.copy(pending = world.pending + (n -> world.time))
          } yield update
        }
        
      case _ => world.pure[F]
    }
    //    for {
    //      stopped <- nodes.traverse(M.stop)
    //      updates = stopped.map(_ -> world.time).toList.toMap
    //      update = world.copy(pending = world.pending ++ updates)
    //    } yield update

    private def symdiff[T](a: Set[T], b: Set[T]): Set[T] = 
      (a union b) -- (a intersect b)
  }

  /**
    * this is a smart version of if
    */
  private object NeedsAgent {
    def unapply(world: WorldView): Option[MachineNode] = world match {
      case WorldView(backlog, 0, managed, alive, pending, _) 
        if backlog > 0 && alive.isEmpty && pending.isEmpty
          => Option(managed.head)
      case _ => None
    }
  }
  
  private object Stale {
    def unapply(world: WorldView): Option[NonEmptyList[MachineNode]] = world match {
      case WorldView(backlog, _, _, alive, pending, time)
        if alive.nonEmpty =>
          (alive -- pending.keys).collect {
            case (n, started) if backlog == 0 && (time - started).toMinutes % 60 >= 58 => n
            case (n, started) if (time - started) >= 5.hours => n
          }.toList.toNel.toOption
      case _ => None
    }
  }

  /**********
    * TESTING
    *********/
  /** 
    * interpolator:
    * https://github.com/propensive/contextual
    * https://propensive.com/opensource/contextual/#contextual
    * compile-time safety!!
    */
  import java.time.Instant
  
  trait Verifier[A]
  object Prefix { def apply(a: Any*): Epoch = ??? }
//  import contextual._

  object EpochInterpolator extends Verifier[Epoch] {
    def check(s: String): Either[(Int, String), Epoch] =
      try Right(Epoch(Instant.parse(s).toEpochMilli))
      catch { 
        case _ => Left((0, "not in ISO-8601 format"))
      }
  }
  implicit class EpochMillisStringContext(sc: StringContext) {
    val epoch = Prefix(EpochInterpolator, sc)
  }
  object Data {
    val node1 = MachineNode("1243d1af-828f-4ba3-9fc0-a19d86852b5a")
    val node2 = MachineNode("550c4943-229e-47b0-b6be-3d686c5f013f")
    val managed = NonEmptyList(node1, node2)
    val time1: Epoch = ??? //epoch"2017-03-03T18:07:00Z"
    val time2: Epoch = ??? //epoch"2017-03-03T18:59:00Z" // +52 mins
    val time3: Epoch = ??? //epoch"2017-03-03T19:06:00Z" // +59 mins
    val time4: Epoch = ??? //epoch"2017-03-03T23:07:00Z" // +5 hours
    val needsAgents = WorldView(5, 0, managed, Map.empty, Map.empty, time1)
  }
  import Data._

  (0, "not in ISO-8601 format")

  class Mutable(state: WorldView) {
    var started, stopped: Int = 0
    
    private val D: Drone[Id] = new Drone[Id] {
      def getBacklog: Int = state.backlog
      def getAgents: Int = state.agents
    }
    
    private val M: Machines[Id] = new Machines[Id] {
      def getAlive: Map[MachineNode, Epoch] = state.alive
      def getManaged: NonEmptyList[MachineNode] = state.managed
      def getTime: Epoch = state.time
      def start(node: MachineNode): MachineNode = { started += 1 ; node }
      def stop(node: MachineNode): MachineNode = { stopped += 1 ; node }
    }
    
    val program = new DynAgentsModule[Id](D, M)
    
  }
  
  val l1: Maybe[NonEmptyList[Int]] = List(1,2,3).toNel
  val l2: Maybe[NonEmptyList[Int]] = List().toNel
  
}
