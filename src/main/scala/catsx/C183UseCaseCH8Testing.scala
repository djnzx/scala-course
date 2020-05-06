package catsx

import cats.{Applicative, Id}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats.instances.future._
import cats.instances.list._
import cats.syntax.functor._
import cats.syntax.traverse._

/**
  * this use case shows the 2 patterns:
  * - F[A]
  * to work with Future[Int] and Id[Int], actually just Int
  * in the same way
  * - Applicative[F]
  * to iterate over something
  */
object C183UseCaseCH8Testing extends App {

  // trait
  trait UptimeClient[F[_]] {
    def getUptime(hostname: String): F[Int]
  }

  // real implementation
  class RealUptimeClient(hosts: Map[String, Int]) extends UptimeClient[Future] {
    override def getUptime(hostname: String): Future[Int] = Future.successful(hosts.getOrElse(hostname, 0)*10)
  }

  // mock
  class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient[Id] {
    override def getUptime(hostname: String): Id[Int] = hosts.getOrElse(hostname, 0)*10
  }

  /**
    * service
    * traverse needs Applicative to work
    * .traverse() on List[Int] - because of:
    * - import cats.syntax.traverse._
    * - import cats.syntax.traverse._
    * .map(), because of:
    * - import cats.syntax.functor._
    */
  class UptimeService[F[_]: Applicative](client: UptimeClient[F]) {
    def getTotalUptime(hostnames: List[String]): F[Int] =
      hostnames.traverse(client.getUptime).map(_.sum)
  }

  val hosts = Map("host1" -> 10, "host2" -> 6)
  // test
  def testTotalUptime(): Unit = {
    println("Running test...")
    val client: UptimeClient[Id] = new TestUptimeClient(hosts)
    val service: UptimeService[Id] = new UptimeService(client)
    val actualInt: Id[Int] = service.getTotalUptime(hosts.keys.toList)
    val expected: Int = hosts.values.sum*10
    assert(actualInt == expected)
  }

  def realTotalUptime(): Unit = {
    println("Running real...")
    val client: UptimeClient[Future] = new RealUptimeClient(hosts)
    val service: UptimeService[Future] = new UptimeService(client)
    val actualFuture: Future[Int] = service.getTotalUptime(hosts.keys.toList)
    val actual: Int = Await.result(actualFuture, 1.seconds)
    val expected: Int = hosts.values.sum*10
    assert(actual == expected)
  }

  realTotalUptime()
  testTotalUptime()
}
