package xml

import scala.xml._

object XMLParse1 extends App {

  val xmlText: String =
    """
      |<html>
      |  <body>hello</body>
      |</html>
      |""".stripMargin

  val xml1: Elem = scala.xml.XML.loadString(xmlText)
  println((xml1 \ "body").text)

  val weather: Elem =
    <rss>
      <channel>
        <title>Yahoo! Weather - Boulder, CO</title>
        <item>
          <title>Conditions for Boulder, CO at 2:54 pm MST</title>
          <forecast day="Thu"
                    date="10 Nov 2011"
                    low="37"
                    high="58"
                    text="Partly Cloudy"
                    code="29" />
        </item>
      </channel>
    </rss>

  val result =
    <persons>
      <total>2</total>
      <someguy>
        <firstname>john</firstname>
        <name>Snow</name>
      </someguy>
      <otherperson>
        <sex>female</sex>
      </otherperson>
    </persons>
//  val result: Elem = scala.xml.XML.loadString(xml)

  def linearize(node: Node, stack: String, map: Map[String, String]): List[(Node, String, Map[String, String])] =
    (node, stack, map) :: node
      .child
      .flatMap {
        case e: Elem =>
          if (e.descendant.size == 1) linearize(e, stack, map ++ Map(stack + "/" + e.label -> e.text))
          else linearize(e, stack + "/" + e.label, map)
        case _ => Nil
      }
      .toList

  val map = linearize(result, "", Map[String, String]()).flatMap(_._3).toMap

  pprint.pprintln(map)

  case class Book(name: String)

  /** https://github.com/CodersBistro/Scalaxb-Examples/tree/master/scalaxb-serde
    */
}
