package ws.core

import cats.implicits.*
import cats.parse.Parser
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import ws.core.InputFrame.TextFrame

class TextInputFrameParserSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import InputFrame.p.*

  test("command parser") {
    def parse(raw: String) = cmdParser.parseAll(raw)

    val dataR = Table(
      "in"    -> "out",
      "/room" -> "room",
      "/go"   -> "go",
      "/a"    -> "a",
    )

    val dataL = Table(
      "in",
      "/",
      "room",
      "go",
      "a",
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

    forAll(dataL) { in =>
      val x = parse(in)
      x.toOption shouldBe None
    }
  }

  test("valid command parser") {
    def parse(raw: String) = validCmdParser.parseAll(raw)

    val dataR = Table(
      "in"       -> "out",
      "/room"    -> "room",
      "/rooms"   -> "rooms",
      "/help"    -> "help",
      "/members" -> "members",
    )

    val dataL = Table(
      "in",
      "/",
      "wrong",
      "go",
      "a",
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

    forAll(dataL) { in =>
      val x = parse(in)
      x.toOption shouldBe None
    }
  }

  test("parameters parser") {
    def parse(raw: String) = parametersParser.parseAll(raw)

    val dataR = Table(
      "in"       -> "out",
      ""         -> List(),
      "a "       -> List("a"),
      "a bc def" -> List("a", "bc", "def"),
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

  }

  test("command with parameters parser") {
    def parse(raw: String) = cmdWithParametersParser.parseAll(raw)

    val dataR = Table(
      "in"              -> "out",
      "/room"           -> ("room" -> List()),
      "/room  scala"    -> ("room" -> List("scala")),
      "/room scala abc" -> ("room" -> List("scala", "abc")),
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

  }

  test("total command parser") {
    def parse(raw: String) = totalCommandParser.parseAll(raw)

    import InputFrame.TextFrame.CommandFrame

    val dataR = Table(
      "in"              -> "out",
      "/room"           -> CommandFrame.CommandValid("room", List()),
      "/rooms"          -> CommandFrame.CommandValid("rooms", List()),
      "/room scala"     -> CommandFrame.CommandValid("room", List("scala")),
      "/room scala abc" -> CommandFrame.CommandValid("room", List("scala", "abc")),
      "/welcome"        -> CommandFrame.CommandValid("welcome", List()),
      "/join me"        -> CommandFrame.CommandValid("join", List("me")),
    )
    val dataL = Table(
      "in",
      "/",
      "/ scala",
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

    forAll(dataL) { in =>
      parse(in).toOption shouldBe None
    }

  }

  test("target parser") {
    def parse(raw: String): Either[Parser.Error, String] = targetParser.parseAll(raw)

    val dataR = Table(
      "in"    -> "out",
      "@jim"  -> "jim",
      "@alex" -> "alex",
      "@x"    -> "x",
    )

    val dataL = Table(
      "in",
      "@",
      "room",
      "go",
      "a",
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

    forAll(dataL) { in =>
      val x = parse(in)
      x.toOption shouldBe None
    }
  }

  test("target with message parser") {
    def parse(raw: String) = privateMessageParser.parseAll(raw)

    val dataR = Table(
      "in"                    -> "out",
      "@jim hello"            -> ("jim", "hello"),
      "@alex nice to see you" -> ("alex", "nice to see you"),
    )

    val dataL = Table(
      "in",
      "@ nice to see you"
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

    forAll(dataL) { in =>
      parse(in).toOption shouldBe None
    }

  }

  test("public message parser") {
    def parse(raw: String) = publicMessageParser.parseAll(raw)

    val dataR = Table(
      "in"                    -> "out",
      "@jim hello"            -> "@jim hello",
      "@alex nice to see you" -> "@alex nice to see you",
    )

    val dataL = Table(
      "in",
      "a"
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

    forAll(dataL) { in =>
      parse(in).toOption shouldBe None
    }

  }

  test("almost total (private or public, without Invalid Message) message parser") {
    def parse(raw: String): Either[Parser.Error, TextFrame.MessageFrame] = privateOrPublicMessageParser.parseAll(raw)

    val dataR = Table(
      "in"                    -> "out",
      "@jim hello"            -> TextFrame.MessageFrame.PrivateChatMessage("jim", "hello"),
      "@alex nice to see you" -> TextFrame.MessageFrame.PrivateChatMessage("alex", "nice to see you"),
      "hello"                 -> TextFrame.MessageFrame.PublicChatMessage("hello"),
      "hello everybody"       -> TextFrame.MessageFrame.PublicChatMessage("hello everybody"),
    )

    val dataL = Table(
      "in",
      "@ hello",
      "x"
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
    }

    forAll(dataL) { in =>
      parse(in).toOption shouldBe None
    }

  }

  test("fallback wrapper") {
    def parse(raw: String): TextFrame.MessageFrame.InvalidMessage = fallback(raw)

    val dataR = Table(
      "in",
      "@",
      "@",
      "/",
      "!@#",
      "whatever",
    )

    forAll(dataR) { in =>
      parse(in) shouldBe TextFrame.MessageFrame.InvalidMessage(in)
    }

  }

  test("TOTAL TextParser, Invalid Message as otherwise case") {
    import ws.core.InputFrame.*

    def parse(raw: String): Either[Parser.Error, TextFrame] = totalInputFrameParser.parseAll(raw)
    def parseTotal(raw: String): TextFrame = totalTextParser(raw)

    val dataR = Table(
      "in"               -> "out",
      "/help"            -> TextFrame.CommandFrame.CommandValid("help", List()),
      "/room"            -> TextFrame.CommandFrame.CommandValid("room", List()),
      "/room scala"      -> TextFrame.CommandFrame.CommandValid("room", List("scala")),
      "/room scala abc"  -> TextFrame.CommandFrame.CommandValid("room", List("scala", "abc")),
      "/welcome"         -> TextFrame.CommandFrame.CommandValid("welcome", List()),
      "/join me"         -> TextFrame.CommandFrame.CommandValid("join", List("me")),
      "hello world"      -> TextFrame.MessageFrame.PublicChatMessage("hello world"),
      "@jim hi"          -> TextFrame.MessageFrame.PrivateChatMessage("jim", "hi"),
      "@tommy follow me" -> TextFrame.MessageFrame.PrivateChatMessage("tommy", "follow me"),
    )

    val dataL = Table(
      "in",
      "x",
      "",
    )

    forAll(dataR) { (in, out) =>
      parse(in) shouldBe out.asRight
      parseTotal(in) shouldBe out
    }

    forAll(dataL) { in =>
      parse(in).toOption shouldBe None
      parseTotal(in) shouldBe TextFrame.MessageFrame.InvalidMessage(in)
    }

  }

}
