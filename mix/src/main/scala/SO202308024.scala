import SO202308024.browser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser

// https://stackoverflow.com/questions/76821092/check-whether-a-key-exists-or-not-in-a-scala-map
object SO202308024 extends App {

  val browser = JsoupBrowser()

  val x: browser.DocumentType = browser.get("https://www.google.com/search?q=java")
  println(x)

}
