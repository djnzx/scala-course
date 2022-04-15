package count_java.strip;

import count_java.strip.Token.*;
import count_java.strip.state.LineState;
import count_java.strip.state.STATE;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static count_java.util.Predef.WRONG_STATE;
import static count_java.util.Predef.WRONG_SYNTAX;

public final class Strip5Wisely implements Strip {

  private static Stream<BiFunction<String, Integer, Optional<Token>>> tokensToFind(LineState ls) {
    switch (ls.state()) {
      case CODE:   return Stream.of(TkQuote::find, TkBlockOp::find, TkLine::find);
      case STRING: return Stream.of(TkQuote::find, TkXQuote::find);
      case BLOCK:  return Stream.of(TkBlockCl::find);
    }
    throw WRONG_STATE;
  }

  private static Optional<Token> nextToken(LineState ls) {
    return tokensToFind(ls)
        .map(f -> f.apply(ls.input, ls.pos))
        .flatMap(ot -> ot.map(Stream::of).orElseGet(Stream::empty))
        .min(Comparator.comparingInt(tk -> tk.at));
  }

  private static Strip nextOperation(STATE state, Optional<Token> ot) {
    switch (state) {
      case CODE: return ot.map((Function<Token, Strip>) t ->
              t instanceof TkBlockOp ? ls -> ls.saveTo(t.at).shift(t.len).swBlock() :
              t instanceof TkLine    ? ls -> ls.saveTo(t.at).skipRest() :
              t instanceof TkQuote   ? ls -> ls.saveTo(t.at + t.len).swString() :
                                       ls -> { throw WRONG_SYNTAX; }
          ).orElseGet(() ->            ls -> ls.saveRest());
      case STRING: return ot.map((Function<Token, Strip>) t ->
              t instanceof TkXQuote  ? ls -> ls.saveTo(t.at + t.len) :
              t instanceof TkQuote   ? ls -> ls.saveTo(t.at + t.len).swString() :
                                       ls -> { throw WRONG_SYNTAX; }
           ).orElseGet(() ->           ls -> ls.saveRest());
      case BLOCK: return ot.map((Function<Token, Strip>) t ->
              t instanceof TkBlockCl ? ls -> ls.moveTo(t.at).shift(t.len).swBlock() :
                                       ls -> { throw WRONG_SYNTAX; }
           ).orElseGet(() ->           ls -> ls.skipRest());
    }
    throw WRONG_STATE;
  }

  public LineState apply(LineState lst) {
    return nextOperation(lst.state(), nextToken(lst)).apply(lst);
  }

}
