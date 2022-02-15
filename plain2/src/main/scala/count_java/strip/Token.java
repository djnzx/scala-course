package count_java.strip;

import java.util.Optional;

public abstract class Token implements Patterns {
  public final int at;
  public final int len;

  protected Token(int at, int len) {
    this.at = at;
    this.len = len;
  }

  public static class TkQuote extends Token {
    protected TkQuote(int at) {
      super(at, QUOTE.length());
    }
    public static Optional<Token> find(String where, int from) {
      return Optional.of(where.indexOf(QUOTE, from)).filter(x -> x >= 0).map(TkQuote::new);
    }
  }

  public static class TkXQuote extends Token {
    protected TkXQuote(int at) {
      super(at, XQUOTE.length());
    }
    public static Optional<Token> find(String where, int from) {
      return Optional.of(where.indexOf(XQUOTE, from)).filter(x -> x >= 0).map(TkXQuote::new);
    }
  }

  public static class TkBlockOp extends Token {
    protected TkBlockOp(int at) {
      super(at, OPEN.length());
    }
    public static Optional<Token> find(String where, int from) {
      return Optional.of(where.indexOf(OPEN, from)).filter(x -> x >= 0).map(TkBlockOp::new);
    }
  }

  public static class TkBlockCl extends Token {
    protected TkBlockCl(int at) {
      super(at, CLOSE.length());
    }
    public static Optional<Token> find(String where, int from) {
      return Optional.of(where.indexOf(CLOSE, from)).filter(x -> x >= 0).map(TkBlockCl::new);
    }
  }

  public static class TkLine extends Token {
    protected TkLine(int at) {
      super(at, LINE.length());
    }
    public static Optional<Token> find(String where, int from) {
      return Optional.of(where.indexOf(LINE, from)).filter(x -> x >= 0).map(TkLine::new);
    }
  }

}
