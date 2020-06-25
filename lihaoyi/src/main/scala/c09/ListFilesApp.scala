package c09

import scalatags.Text
import scalatags.Text.all._
import org.commonmark.node._
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

object ListFilesApp extends App {
  os.remove.all(out)
  os.makeDir.all(out)

  def mdNameToHtml(name: String) = name.replace(" ", "-").toLowerCase + ".html"
  
  val postInfo =
    os.list(posts)
      .map { p =>
        val s"$prefix - $suffix.md" = p.last
        (prefix, suffix, p)
      }
      .sortBy(_._1.toInt)

  val mdParser = Parser.builder().build()
  val renderer = HtmlRenderer.builder().build()

  for ((_, suffix, path) <- postInfo) {
    val document: Node = mdParser.parse(os.read(path))
    val output: String = renderer.render(document)
    os.write(
      out / "post" / mdNameToHtml(suffix),
      doctype("html")(
        html(
          body(
            h1("Blog", "/", suffix),
            raw(output)
          )
        )
      )
    )
  }
  
  val contents: Text.all.doctype = doctype("html")(
    html(
      body(
        h1("Blog"),
        for {
          (_, suffix, _) <- postInfo
        } yield h2(suffix)
      )
    )
  )

  os.write(
    out / "index.html",
    contents
  )
  
}
