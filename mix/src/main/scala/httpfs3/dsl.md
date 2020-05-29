Path
Root
->
/
/:
:?
+&
~

String
IntVar
LongVar
UUIDVar

QueryParamDecoder                          - implicit mapper A => B (Int => Year)
QueryParamDecoderMatcher                   - just match the parameter by name and type 
OptionalQueryParamDecoderMatcher           - just match the OPTIONAL parameter by name and type
QueryParamMatcher
OptionalMultiQueryParamDecoderMatcher
OptionalQueryParamMatcher
FlagQueryParamMatcher                      - valueless parameter
ValidatingQueryParamDecoderMatcher         - just match the parameter by name and type VALID
OptionalValidatingQueryParamDecoderMatcher - just match the OPTIONAL parameter by name and type VALID
