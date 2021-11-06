package udemy.scala_beginners.lectures.part3fp._try_succ_fail

import scala.util.{Random, Try}

/**
  * Created by Daniel.
  */
object HandlingFailureHttpExample extends App {

  val host = "localhost"
  val port = "8080"
  def renderHTML(page: String) = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }

    def getSafe(url: String): Try[String] = Try(get(url))
  }

  object HttpService {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection =
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the port")

    def getSafeConnection(host: String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  // if you get the html page from the connection, print it to the console i.e. call renderHTML
  val possibleConnection = HttpService.getSafeConnection(host, port)
  val possibleHTML = possibleConnection.flatMap(connection => connection.getSafe("/home"))
  possibleHTML.foreach(renderHTML)

  // classic Java-way
  try {
    val connection = HttpService.getConnection(host, port)
    try {
      val page = connection.get("/home")
      renderHTML(page)
    } catch {
      case e:Exception => println(s"caught page getting error: ${e.getMessage}")
    }
  } catch {
    case e:Exception => println(s"caught connection error: ${e.getMessage}")
  }

  // plain way with if handling
  val connection = HttpService.getSafeConnection(host, port) // Try[Connection]
  if (connection.isSuccess) {
    val page = connection.flatMap(connection => connection.getSafe("/home"))
    if (page.isSuccess) {
      page.foreach(renderHTML)
    }
  }

  // for-comprehension version
  for {
    connection <- HttpService.getSafeConnection(host, port) // Try[Connection]
    html <- connection.getSafe("/home") // Try[String]
  } renderHTML(html)

  // shorthand version
  HttpService.getSafeConnection(host, port)
      .flatMap(connection => connection.getSafe("/home"))
      .foreach(renderHTML)

}
