package http4s1.uri_ideas

import org.http4s.Request
import org.http4s.Uri

object RequestUriSyntax {

  implicit class UriAddParentSyntax(private val uri: Uri) extends AnyVal {
    def prepend(parentUri: Uri): Uri = parentUri.resolve(uri)
  }

  implicit class RequestUriAddParentSyntax[F[_]](private val rq: Request[F]) extends AnyVal {
    def uriPrepend(parentUri: Uri): Request[F] = rq.withUri(rq.uri.prepend(parentUri))
  }

}
