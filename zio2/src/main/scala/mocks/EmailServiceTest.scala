package mocks

import api._
import impl.UserServiceLive
import mocks.MockEmailService
import mocks.MockUserRepository
import zio._
import zio.mock._
import zio.test._

// https://github.com/zio/zio-mock
// TODO: https://github.com/kitlangton/stubby
object EmailServiceTest extends ZIOSpecDefault {

  /** emailService call */
  def test1 = test("non-adult registration") {
    // our function to test
    val app: ZIO[UserService, String, Unit] = UserService.register("john", 15, "john@doe")

    // our service as layer
    val liveUserService: URLayer[EmailService with UserRepository, UserService] = UserServiceLive.layer

    // don't do anything with
    val mockUserRepo = MockUserRepository.empty

    val mockEmailService: Expectation[EmailService] = MockEmailService.Send(
      // we expect method was called with these parameters
      assertion = Assertion.equalTo(("john@doe", "You are not eligible to register!")),
      // and return is unit
      result = Expectation.unit,
    )

    // dependencies provided
    val app0: ZIO[Any, String, Unit] = app.provide(liveUserService, mockUserRepo, mockEmailService)

    app0
      .as(assertTrue(true))
  }

  def test2 = test("a valid user can register to the user service") {
    val app: ZIO[UserService, String, Unit] = UserService.register("jane", 25, "jane@doe")
    val liveUserService = UserServiceLive.layer
    val mockUserRepo: Expectation[UserRepository] = MockUserRepository.Save(
      Assertion.equalTo(User("jane", 25, "jane@doe")),
      Expectation.unit
    )
    val mockEmailService = MockEmailService.Send(
      assertion = Assertion.equalTo(("jane@doe", "Congratulation, you are registered!")),
      result = Expectation.unit
    )

    val app0: ZIO[Any, String, Unit] = app.provide(liveUserService, mockUserRepo, mockEmailService)

    app0
      .as(assertTrue(true))
  }

  // error channel test
  def test3 = test("user cannot register pre-defined admin user") {
    val app = UserService.register("admin", 30, "admin@doe")

    def postCheck(outcome: Exit[String, Unit]) =
      outcome match {
        case Exit.Failure(cause)
            if cause.contains(
              Cause.fail("The admin user is already registered!")
            ) => true
        case _ => false
      }

    // Exit[E, A] is a result of ZIO
    for {
      outcome <- app.provide(
                   UserServiceLive.layer,
                   MockEmailService.empty,
                   MockUserRepository.empty
                 ).exit
    } yield assertTrue(postCheck(outcome))
  }

  override def spec: Spec[TestEnvironment with Scope, Any] = suite("0")(
    test1,
    test2,
    test3,
  )
}
