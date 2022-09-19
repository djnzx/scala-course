package object k8x {

  final implicit class StringDeCapitalizeSyntax(private val s: String) extends AnyVal {

    def deCapitalize: String = s match {
      case s if s.isEmpty => s
      case s              =>
        val chars: Array[Char] = s.toCharArray
        chars(0) = Character.toLowerCase(chars(0))
        new String(chars)
    }

  }

}
