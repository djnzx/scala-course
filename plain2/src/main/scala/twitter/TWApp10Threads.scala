package twitter

import java.io.{InputStream, OutputStream}
import java.net.{ServerSocket, Socket}
import java.util.concurrent.{ExecutorService, Executors}

/**
  * https://twitter.github.io/scala_school/concurrency.html
  */
object TWApp10Threads extends App {

  class NetworkService(port: Int, poolSize: Int) {
    val serverSocket = new ServerSocket(port)
    val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

    def run {
      try {
        while (true) {
          val socket = serverSocket.accept()
          pool.execute(new Handler(socket))
        }
      } finally {
        pool.shutdown()
      }
    }
  }

  class Handler(socket: Socket) extends Runnable {
    def message = Thread.currentThread.getName

    def run() {
      println(message)
      val is: InputStream = socket.getInputStream
      val os: OutputStream = socket.getOutputStream
//      val read: Array[Byte] = is.readAllBytes()
//      println(read.length)
//      println(read.toList)
//      os.write(message)
      os.close()
      is.close()
    }
  }

  (new NetworkService(2020, 5)).run

}
