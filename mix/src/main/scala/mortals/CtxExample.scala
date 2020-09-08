package mortals

object CtxExample {
  import contextual._

  case class Url(url: String)

  object UrlInterpolator extends Interpolator {

    type Out = Url

    def checkValidUrl(s: String) = s.contains("@")
    
    def contextualize(interpolation: StaticInterpolation) = {
      val lit @ Literal(_, urlString) = interpolation.parts.head
      if(!checkValidUrl(urlString))
        interpolation.abort(lit, 0, "not a valid URL")

      Nil
    }

    def evaluate(interpolation: RuntimeInterpolation): Url =
      Url(interpolation.literals.head)
  }

  implicit class UrlStringContext(sc: StringContext) {
    val url = Prefix(UrlInterpolator, sc)
  }

//    val x: UrlInterpolator.Output = url"http://www.propensive.com/"
//    println(x)
}
