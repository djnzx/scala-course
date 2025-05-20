package djnxz.ai

import cats.effect.IO
import cats.effect.IOApp
import fs2.Stream
import sttp.client4.httpclient.fs2.HttpClientFs2Backend
import sttp.openai.OpenAI
import sttp.openai.OpenAIExceptions.OpenAIException
import sttp.openai.requests.completions.chat.ChatChunkRequestResponseData.ChatChunkResponse
import sttp.openai.requests.completions.chat.ChatRequestBody.ChatBody
import sttp.openai.requests.completions.chat.ChatRequestBody.ChatCompletionModel
import sttp.openai.requests.completions.chat.message._
import sttp.openai.streaming.fs2._

object Playground extends IOApp.Simple {

  val apiKey = ???

  override def run: IO[Unit] = {
    val openAI = new OpenAI(apiKey)

    val q =
      """
        |I need a list of the most recent 3-5 top book printed in US
        |with content similar to
        |Бурбаки Н. Общая топология. Основные структуры
        |reference should conform ДСТУ 8302:2015
        |title, author and publisher keep in english
        |output please for into json with fields: title, authors, year, publisher, dstu
        |in the field dstu please keep everything in english
        |""".stripMargin

    val bodyMessages: Seq[Message] = Seq(
      Message.UserMessage(
        content = Content.TextContent(q),
      )
    )

    val chatRequestBody: ChatBody = ChatBody(
      model = ChatCompletionModel.GPT4o,
      messages = bodyMessages
    )

    HttpClientFs2Backend.resource[IO]()
      .use { backend =>

        val response: IO[Either[OpenAIException, Stream[IO, ChatChunkResponse]]] =
          openAI
            .createStreamedChatCompletion[IO](chatRequestBody)
            .send(backend)
            .map(_.body)

        response
          .flatMap {
            case Left(exception) => IO.println(exception.getMessage)
            case Right(s)        => s
                .flatMap(xs => fs2.Stream.emits(xs.choices))
                .map(_.delta.content)
                .unNone
                .compile
                .string
                .flatMap(IO.println)
          }
      }
  }
}
