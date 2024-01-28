package app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Launcher

object Launcher extends App {

  SpringApplication.run(classOf[Launcher], args: _*)

}
