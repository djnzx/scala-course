package lesson59.warmup;

public class CmdVisitChild implements Command {
  public final int n;

  public CmdVisitChild(int n) {
    this.n = n;
  }

  public CmdVisitChild(String raw) {
    this(
        Integer.parseInt(
            raw.substring("visit child ".length()).trim()
        )
    );
  }

}
