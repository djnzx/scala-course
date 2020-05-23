package warmup;

import java.util.stream.Collectors;

public class SwitchCaseJava {
  String invert(String origin) {
    return origin.chars()
        .map(c -> (c ^ 32))
        .mapToObj(x -> String.valueOf((char)x))
        .collect(Collectors.joining());
  }

  public static void main(String[] args) {
    SwitchCaseJava is = new SwitchCaseJava();
    System.out.println(is.invert("abc"));
  }
}
