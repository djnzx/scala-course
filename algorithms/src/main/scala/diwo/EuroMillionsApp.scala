package diwo

import diwo.Domain._
import diwo.ExtendedSyntax._

import scala.util.Using

/** main app runner */
object EuroMillionsApp extends App {

  def process(draw: String, tickets: String) =
    Using.resources(
      scala.io.Source.fromFile(fs.resourceOrDie(draw)),
      scala.io.Source.fromFile(fs.resourceOrDie(tickets))
    ) { case (dr, ts) =>
      dr.getLines()
        .nextOption()
        .toRight(!msg.file_is_empty(draw))
        .flatMap(Draw.parse)
        .foreach { d: Draw =>
          val tss = ts.getLines().flatMap(Ticket(_)).toSeq
          val out = Results.calculate(d, tss)
          Results.represent(out)
            .foreach(println)
        }
    }

  process("draw.txt", "tickets.txt")
}
