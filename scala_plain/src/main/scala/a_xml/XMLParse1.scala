package a_xml

import scala.xml.Elem

object XMLParse1 extends App {

  val xmlText: String =
    """
      |<html>
      |  <body>hello</body>
      |</html>
      |""".stripMargin
      
  val xml: Elem = scala.xml.XML.loadString(xmlText)
  println((xml \ "body").text)

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

  
}
