package app

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import scala.jdk.CollectionConverters.IterableHasAsJava

@RestController
class Endpoints {

  @GetMapping(Array("/all"))
  def all =
    List(
      Person(1, "Jim"),
      Person(2, "Jack"),
    ).asJava

}
