package c09

import scalatags.Text.all._

object HtmlApp extends App {
  val postInfo = Vector(
    (1,"qw",4),
    (2,"as",5),
    (3,"zx",6),
  )
  
//  os.write(
//    os.pwd / "out" / "index.html", doctype("html")(
//      html( body(
//        h1("Blog"),
//        for ((_, suffix, _) <- postInfo)
//          yield h2(a(href := ("post/" + mdNameToHtml(suffix)))(suffix))
//      ) )
//    )
//  )
}
