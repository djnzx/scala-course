package http_param_matcher

import SealedTraitDecoder.customQueryParamDecoder
import org.http4s.dsl.io.QueryParamDecoderMatcher

object FruitParamMatcher extends QueryParamDecoderMatcher[Fruit]("f")