package interview.pragmatic;

public class Warmup1 {

  public static void main(String[] args) {
    final String s = "10101";
    String target = s.replace("0", "") + s.replace("1","");
    System.out.println(target);
  }

}
