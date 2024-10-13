package lesson59.warmup;

public class CmdChangeValue implements Command {
  public final int val;

  public CmdChangeValue(int val) {
    this.val = val;
  }

  public CmdChangeValue(String raw) {
    this(
        Integer.parseInt(
            raw.substring("change ".length()).trim()
        )
    );
  }
}
